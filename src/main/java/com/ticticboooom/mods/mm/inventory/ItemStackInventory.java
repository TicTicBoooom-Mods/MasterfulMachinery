package com.ticticboooom.mods.mm.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackInventory  implements IInventory {

    private ItemStackHandler handler;

    public ItemStackInventory(ItemStackHandler handler) {

        this.handler = handler;
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return handler.getStackInSlot(p_70301_1_);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        ItemStack stackInSlot = handler.getStackInSlot(p_70298_1_);
        handler.setStackInSlot(p_70298_1_, ItemStack.EMPTY);
        return stackInSlot;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return handler.getStackInSlot(p_70304_1_);
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        handler.setStackInSlot(p_70299_1_, p_70299_2_);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        return true;
    }

    @Override
    public void clearContent() {

    }
}
