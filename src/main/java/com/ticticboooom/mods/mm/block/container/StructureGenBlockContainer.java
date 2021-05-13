package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.tile.ProjectorBlockEntity;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;

public class StructureGenBlockContainer extends Container {
    private StructureGenBlockEntity tile;

    public StructureGenBlockContainer(int windowId, StructureGenBlockEntity tile) {
        super(MMSetup.STRUCTURE_CONTAINER.get(), windowId);
        this.tile = tile;
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }

    public StructureGenBlockContainer(int w, PlayerInventory inv, PacketBuffer buf) {
        this(w, (StructureGenBlockEntity) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }
}
