package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.GuiHelper;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.inventory.mek.PortMekInfuseInventory;
import com.ticticboooom.mods.mm.inventory.mek.PortMekSlurryInventory;
import lombok.Getter;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class MekInfusePortStorage extends PortStorage {
    public static final Codec<MekInfusePortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.LONG.fieldOf("capacity").forGetter(z -> z.capacity)
    ).apply(x, MekInfusePortStorage::new));

    @Getter
    private final PortMekInfuseInventory inv;
    private long capacity;
    private final LazyOptional<PortMekInfuseInventory> invLO;


    public MekInfusePortStorage(long capacity) {
        inv = new PortMekInfuseInventory(capacity);
        this.capacity = capacity;
        invLO = LazyOptional.of(() -> inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == Capabilities.INFUSION_HANDLER_CAPABILITY;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("infuse", inv.getStack().getType().getRegistryName().toString());
        nbt.putLong("amount", inv.getStack().getAmount());
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        if (nbt.contains("infuse")){
            inv.setStack(new InfusionStack(Objects.requireNonNull(MekanismAPI.infuseTypeRegistry().getValue(RLUtils.toRL(nbt.getString("infuse")))), nbt.getLong("amount")));
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        int barX = left + 175 - 30;
        int barY = top + 20;
        int barWidth = 18;
        int barHeight = 108;
        screen.blit(stack, barX, barY, 175, 18, barWidth, barHeight);
        float pct = 0;
        if (inv.getStack().getAmount() > 0) {
            pct = (float) inv.getStack().getAmount() / inv.getTankCapacity(0);
        }
        GuiHelper.renderVerticallyFilledBar(stack, screen, barX, barY, 193, 18, barWidth, barHeight, pct);
        AbstractGui.drawString(stack, Minecraft.getInstance().fontRenderer, inv.getStack().getType().getTextComponent().getString(), left + 30, top + 60, 0xfefefe);
        AbstractGui.drawString(stack, Minecraft.getInstance().fontRenderer, inv.getStack().getAmount() + "mB", left + 30, top + 80, 0xfefefe);
    }
}
