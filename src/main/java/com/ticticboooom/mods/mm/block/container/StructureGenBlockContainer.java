package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.container.slot.StructureDeviceSlot;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class StructureGenBlockContainer extends Container {
    @Getter
    private StructureGenBlockEntity tile;

    public StructureGenBlockContainer(int windowId, PlayerInventory inv, StructureGenBlockEntity tile) {
        super(MMSetup.STRUCTURE_CONTAINER.get(), windowId);
        this.tile = tile;

        this.addSlot(new StructureDeviceSlot(tile.getInv(), 0, 151, -14));

        this.tile = tile;
        int playerOffsetX = 8;
        int playerOffsetY = 121;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(inv, 9 + (j * 9 + i), i * 18 + playerOffsetX, j * 18 + playerOffsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + (i * 18), 179));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        return true;
    }

    public StructureGenBlockContainer(int w, PlayerInventory inv, PacketBuffer buf) {
        this(w, inv, (StructureGenBlockEntity) inv.player.world.getTileEntity(buf.readBlockPos()));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if (slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < 1) {
                if (!this.mergeItemStack(itemStack1, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }
}
