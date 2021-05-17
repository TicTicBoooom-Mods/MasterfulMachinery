package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ProjectorBlock extends Block {
    public ProjectorBlock() {
        super(Properties.of(Material.METAL));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    private static final DirectionProperty FACING = HorizontalBlock.FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 8, 0, 4, 10, 16),
            Block.box(4, 8, 8, 16, 12, 16),
            Block.box(12, 8, 0, 16, 10, 8),
            Block.box(5, 8, 1, 11, 12, 7),
            Block.box(5, 12, 9, 11, 16, 15)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 8, 0, 16, 10, 4),
            Block.box(0, 8, 4, 8, 12, 16),
            Block.box(8, 8, 12, 16, 10, 16),
            Block.box(9, 8, 5, 15, 12, 11),
            Block.box(1, 12, 5, 7, 16, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(12, 8, 0, 16, 10, 16),
            Block.box(0, 8, 0, 12, 12, 8),
            Block.box(0, 8, 8, 4, 10, 16),
            Block.box(5, 8, 9, 11, 12, 15),
            Block.box(5, 12, 1, 11, 16, 7)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.box(0, 0, 0, 16, 8, 16),
            Block.box(0, 8, 12, 16, 10, 16),
            Block.box(8, 8, 0, 16, 12, 12),
            Block.box(0, 8, 0, 8, 10, 4),
            Block.box(1, 8, 5, 7, 12, 11),
            Block.box(9, 12, 5, 15, 16, 11)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();


    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MMSetup.PROJECTOR_TILE.get().create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return SHAPE_N;
            case EAST:
                return SHAPE_E;
            case SOUTH:
                return SHAPE_S;
            case WEST:
                return SHAPE_W;
            default:
                throw new IllegalStateException("Invalid State");
        }
    }
}
