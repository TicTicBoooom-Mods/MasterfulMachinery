package com.ticticboooom.mods.mm.client.container;


import com.ticticboooom.mods.mm.block.tile.ProjectorTile;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class ProjectorContainer extends Container {
    public ProjectorTile tile;
    public PlayerInventory inv;

    public ProjectorContainer(ProjectorTile tile, PlayerInventory inv, int id) {
        super(MMContainerTypes.PROJECTOR.get(), id);
        this.tile = tile;
        this.inv = inv;

        addSlot(new Slot(tile.blueprint, 0, 152, -9));

        int playerOffsetX = 8;
        int playerOffsetY = 121;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                addSlot(new Slot(inv, 9 + (j * 9 + i), i* 18 + playerOffsetX, j* 18 + playerOffsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inv, i,8 + (i * 18), 179));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public ProjectorContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this((ProjectorTile) inv.player.world.getTileEntity(data.readBlockPos()), inv, windowId);
    }
}
