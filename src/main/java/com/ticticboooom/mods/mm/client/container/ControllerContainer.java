package com.ticticboooom.mods.mm.client.container;

import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

public class ControllerContainer extends Container {
    private ControllerTile tile;
    private PlayerInventory inv;

    public ControllerContainer(ControllerTile tile, PlayerInventory inv, int id) {
        super(MMContainerTypes.CONTROLLER.get(), id);
        this.tile = tile;
        this.inv = inv;
    }

    public ControllerContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this((ControllerTile) inv.player.world.getTileEntity(data.readBlockPos()), inv, windowId);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public ControllerTile getTile() {
        return tile;
    }
}
