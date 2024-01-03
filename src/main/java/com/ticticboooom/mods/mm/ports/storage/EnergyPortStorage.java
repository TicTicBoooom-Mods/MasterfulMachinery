package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.GuiHelper;
import com.ticticboooom.mods.mm.inventory.PortEnergyInventory;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyPortStorage extends PortStorage {
    public static final Codec<EnergyPortStorage> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.inv.getMaxEnergyStored())
        ).apply(x, EnergyPortStorage::new));

    @Getter
    private final PortEnergyInventory inv;
    private final LazyOptional<PortEnergyInventory> invLO;

    public EnergyPortStorage(int capacity) {
        this.inv = new PortEnergyInventory(0, capacity);
        invLO =  LazyOptional.of(() -> this.inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityEnergy.ENERGY;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("stored", inv.getEnergyStored());
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        if (nbt.contains("stored")) {
            inv.setStored(nbt.getInt("stored"));
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft instance = Minecraft.getInstance();
        instance.textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0,  175, 256);
        int barX = left + 175 - 30;
        int barY = top + 20;
        int barWidth = 18;
        int barHeight = 108;
        screen.blit(stack, barX, barY, 175, 18, barWidth, barHeight);
        float pct = 0;
        if (inv.getMaxEnergyStored() > 0) {
            pct = (float)inv.getEnergyStored() / inv.getMaxEnergyStored();
        }
        GuiHelper.renderVerticallyFilledBar(stack, screen, barX, barY, 193, 18, barWidth, barHeight, pct);
        AbstractGui.drawString(stack, instance.fontRenderer,String.format("%.2f",Math.round((float)10000 * pct) / 100.f) + "%", left + 30, top + 60, 0xfefefe);
        AbstractGui.drawString(stack, instance.fontRenderer, inv.getEnergyStored() + "FE", left + 30, top + 80, 0xfefefe);
    }
}
