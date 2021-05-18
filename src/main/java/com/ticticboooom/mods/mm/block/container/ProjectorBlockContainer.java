package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.tile.ProjectorBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

public class ProjectorBlockContainer extends Container {
    private ProjectorBlockEntity tile;

    public ProjectorBlockContainer(int windowId, ProjectorBlockEntity tile) {
        super(MMSetup.PROJECTOR_CONTAINER.get(), windowId);
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        return true;
    }

    public ProjectorBlockContainer(int w, PlayerInventory inv, PacketBuffer buf) {
        this(w, (ProjectorBlockEntity) inv.player.world.getTileEntity(buf.readBlockPos()));
    }
}
