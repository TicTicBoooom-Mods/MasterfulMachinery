package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.inventory.as.PortStarlightInventory;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class StarlightPortStorage extends PortStorage {
    public static final Codec<StarlightPortStorage> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.getInv().getMaxStarlightStored())
    ).apply(x, StarlightPortStorage::new));

    @Getter
    private PortStarlightInventory inv;

    public StarlightPortStorage(int capacity) {
        this.inv = new PortStarlightInventory(0, capacity);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return null;
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("starlight", inv.getStarlightStored());
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        inv.setStored(nbt.getInt("starlight"));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft instance = Minecraft.getInstance();
        instance.textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        AbstractGui.drawCenteredString(stack, instance.fontRenderer, inv.getStarlightStored() + " Starlight", left + 60, top + 30, 0xfefefe);
    }
}
