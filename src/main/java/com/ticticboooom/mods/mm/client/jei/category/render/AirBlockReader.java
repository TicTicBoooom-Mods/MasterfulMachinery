package com.ticticboooom.mods.mm.client.jei.category.render;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AirBlockReader implements IBlockReader {
    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return null;
    }
}
