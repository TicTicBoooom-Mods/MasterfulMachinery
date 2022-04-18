package com.ticticboooom.mods.mm.ports.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.client.container.PortContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.awt.*;

public abstract class PortStorage {
    public abstract <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction);
    public abstract CompoundNBT save(CompoundNBT nbt);
    public abstract void load(CompoundNBT nbt);
    public abstract void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen);
    public void setupContainer(PortContainer container, PlayerInventory inv, TileEntity tile) {
        int playerOffsetX = 8;
        int playerOffsetY = 121;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                container.addSlot(new Slot(inv, 9 + (j * 9 + i), i* 18 + playerOffsetX, j* 18 + playerOffsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            container.addSlot(new Slot(inv, i,8 + (i * 18), 179));
        }
    }
}
