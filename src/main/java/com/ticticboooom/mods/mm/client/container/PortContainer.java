package com.ticticboooom.mods.mm.client.container;

import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PortContainer extends Container {
    private PortTile tile;

    public PortContainer(PortTile tile, PlayerInventory inv,  int id) {
        super(MMContainerTypes.PORT.get(), id);
        this.tile = tile;
        tile.storage.setupContainer(this, inv, tile);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public PortContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this((PortTile) inv.player.world.getTileEntity(data.readBlockPos()), inv, windowId);
    }

    public PortTile getTile() {
        return tile;
    }

    @Override
    public Slot addSlot(Slot slot) {
        return super.addSlot(slot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Object o = tile.storage.getLazyOptional(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        if (o instanceof ItemStackHandler) {
            ItemStackHandler handler = ((ItemStackHandler) o);
            Slot slot = this.getSlot(index);
            if (slot.getHasStack()) {
                ItemStack itemStack1 = slot.getStack();
                itemStack = itemStack1.copy();
                if (index < handler.getSlots()) {
                    if (!this.mergeItemStack(itemStack1, handler.getSlots(), this.inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(itemStack1, 0, handler.getSlots(), false)) {
                    return ItemStack.EMPTY;
                }

                if (itemStack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }
            }
        }
        return itemStack;
    }
}
