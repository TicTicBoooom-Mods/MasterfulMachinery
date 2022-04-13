package com.ticticboooom.mods.mm.block;

import com.simibubi.create.content.contraptions.base.IRotate;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fml.RegistryObject;

public class RotationMachinePortBlock extends MachinePortBlock implements IRotate {

    public RotationMachinePortBlock(RegistryObject<TileEntityType<?>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay, ResourceLocation portTypeId) {
        super(type, name, controllerId, textureOverride, overlay, portTypeId);
    }

    @Override
    public boolean hasShaftTowards(IWorldReader iWorldReader, BlockPos blockPos, BlockState blockState, Direction direction) {
        return true;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return Direction.Axis.X;
    }
}
