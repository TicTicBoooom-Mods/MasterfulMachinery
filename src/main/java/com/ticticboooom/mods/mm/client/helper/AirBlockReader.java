package com.ticticboooom.mods.mm.client.helper;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class AirBlockReader implements IBlockReader {
    private TileEntity tile;
    private BlockState state;

    public AirBlockReader(BlockState state) {
        this.state = state;
    }

    public void setTile(TileEntity t){
        tile = t;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return tile;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return state;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return null;
    }
}