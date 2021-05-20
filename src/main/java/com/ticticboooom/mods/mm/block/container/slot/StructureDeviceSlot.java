package com.ticticboooom.mods.mm.block.container.slot;

import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class StructureDeviceSlot extends Slot {
    public StructureDeviceSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == MMSetup.STRUCTURE_DEVICE.get();
    }
}
