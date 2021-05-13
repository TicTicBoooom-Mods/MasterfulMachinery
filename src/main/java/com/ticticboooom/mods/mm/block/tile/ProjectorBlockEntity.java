package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class ProjectorBlockEntity extends TileEntity {
    public ProjectorBlockEntity() {
        super(MMSetup.PROJECTOR_TILE.get());
    }
}
