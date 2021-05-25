package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import lombok.Getter;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import me.desht.pneumaticcraft.common.util.DirectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PneumaticPortStorage extends PortStorage {

    public static final Codec<PneumaticPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.FLOAT.fieldOf("dangerPressure").forGetter(z -> z.danger),
            Codec.FLOAT.fieldOf("criticalPressure").forGetter(z -> z.critical),
            Codec.INT.fieldOf("volume").forGetter(z -> z.volume)
    ).apply(x, PneumaticPortStorage::new));

    @Getter
    private MachineAirHandler inv;
    private final LazyOptional<MachineAirHandler> invLO;
    private float danger;
    private float critical;
    private int volume;
    private final Map<IAirHandlerMachine, List<Direction>> airHandlerMap = new HashMap<>();

    public PneumaticPortStorage(float danger, float critical, int volume) {
        this.danger = danger;
        this.critical = critical;
        this.volume = volume;
        inv = new MachineAirHandler(danger, critical, volume);
        invLO = LazyOptional.of(() -> inv);
        neighborChanged();
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("air", inv.getAir());
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        inv.addAir(-inv.getAir());
        inv.addAir(nbt.getInt("air"));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        int barOffsetX = 175 - 30;
        int barOffsetY = 20;
        screen.blit(stack, left + barOffsetX, top + barOffsetY, 175, 18, 18, 108);
        float amount = inv.getPressure() / inv.getCriticalPressure();
        screen.blit(stack, left + barOffsetX, top + barOffsetY, 193, 18, 18, (int) (108 * amount));

        AbstractGui.drawCenteredString(stack, Minecraft.getInstance().fontRenderer, NumberFormat.getInstance().format(inv.getPressure()) + "P", left + 50, top + 80, 0xfefefe);
        AbstractGui.drawCenteredString(stack, Minecraft.getInstance().fontRenderer, inv.getAir() + " Air", left + 50, top + 60, 0xfefefe);

    }

    @Override
    public void tick(IMachinePortTile tile) {
        this.inv.tick((TileEntity) tile);
    }

    @Override
    public void neighborChanged() {
        airHandlerMap.clear();
        for (Direction side : DirectionUtil.VALUES) {
            getLO().ifPresent(handler -> airHandlerMap.computeIfAbsent((IAirHandlerMachine) handler, k -> new ArrayList<>()).add(side));
        }
        airHandlerMap.forEach(IAirHandlerMachine::setConnectedFaces);
    }
}
