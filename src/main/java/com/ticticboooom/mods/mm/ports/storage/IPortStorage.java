package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IPortStorage {
    <T> LazyOptional<T> getLO();
    <T> boolean validate(Capability<T> cap);
    CompoundNBT save(CompoundNBT nbt);
    void load(CompoundNBT nbt);
    void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen);
    void setupContainer(PortBlockContainer container, PlayerInventory inv, MachinePortBlockEntity tile);
}
