package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

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

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (!level.isClientSide()) {
            TileEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ControllerBlockEntity) {
                NetworkHooks.openGui(((ServerPlayerEntity) player), (StructureGenBlockEntity)blockEntity, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
