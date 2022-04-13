package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.GuiHelper;
import com.ticticboooom.mods.mm.inventory.botania.PortManaInventory;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ManaPortStorage extends PortStorage {
    public static final Codec<ManaPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.inv.getMaxManaStored())
    ).apply(x, ManaPortStorage::new));

    @Getter
    private final PortManaInventory inv;
    private final LazyOptional<PortManaInventory> invLO;

    public ManaPortStorage(int capacity) {
        this.inv = new PortManaInventory(0, capacity);
        invLO =  LazyOptional.of(() -> this.inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("stored", inv.getManaStored());
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
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/mana_gui.png"));
        screen.blit(stack, left, top, 0, 0,  175, 256);
        int barOffsetX = 175 - 30;
        int barOffsetY = 20;
        int barX = left + 175 - 30;
        int barY = top + 20;
        int barWidth = 18;
        int barHeight = 108;
        screen.blit(stack, barX, barY, 175, 18, barWidth, barHeight);
        float pct = 0;
        if (inv.getMaxManaStored() > 0) {
            pct = (float)inv.getManaStored() / inv.getMaxManaStored();
        }
        GuiHelper.renderVerticallyFilledBar(stack, screen, barX, barY, 193, 18, barWidth, barHeight, pct);
        AbstractGui.drawString(stack, Minecraft.getInstance().fontRenderer, inv.getManaStored() + " Mana", left + 30, top + 60, 0xfefefe);
    }
}
