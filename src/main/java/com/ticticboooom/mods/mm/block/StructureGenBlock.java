package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class StructureGenBlock extends Block {
    public StructureGenBlock() {
        super(Properties.of(Material.METAL));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MMSetup.STRUCTURE_TILE.get().create();
    }
}
