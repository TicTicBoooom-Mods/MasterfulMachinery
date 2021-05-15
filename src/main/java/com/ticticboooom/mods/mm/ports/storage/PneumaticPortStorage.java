package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import lombok.Getter;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

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

    public PneumaticPortStorage(float danger, float critical, int volume) {
        this.danger = danger;
        this.critical = critical;
        this.volume = volume;
        inv = new MachineAirHandler(danger, critical, volume);
        invLO = LazyOptional.of(() -> inv);
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
        CompoundNBT compound = new CompoundNBT();
        nbt.putFloat("pressure", inv.getPressure());
        return compound;
    }

    @Override
    public void load(CompoundNBT nbt) {
        inv.setPressure(nbt.getFloat("pressure"));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }

    @Override
    public void tick(MachinePortBlockEntity tile) {
        this.inv.tick(tile);
    }
}
