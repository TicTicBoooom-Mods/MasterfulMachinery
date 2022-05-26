package com.ticticboooom.mods.mm.client.container.slot;

import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class BlueprintSlot extends Slot {
    public int index;

    public BlueprintSlot(int index, int xPosition, int yPosition) {
        super(new Inventory(1), 0, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return false;
    }

    public void setItem(ItemStack stack) {
        inventory.setInventorySlotContents(0, stack);
    }
}
