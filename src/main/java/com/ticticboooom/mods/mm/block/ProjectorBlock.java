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
        super(Properties.create(Material.IRON));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(4, 2, 4, 12, 3, 12),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(6, 2.5, 6, 10, 3.5, 10),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(5, 6, 5, 11, 12, 11),
            Block.makeCuboidShape(0.75, 2.5, 6.75, 2.25, 4.75, 9.25),
            Block.makeCuboidShape(13.75, 2.5, 6.75, 15.25, 4.75, 9.25),
            Block.makeCuboidShape(6, 3.5, 14, 7, 12.75, 15),
            Block.makeCuboidShape(5.75, 11.25, 13.75, 7.25, 13.5, 15.25),
            Block.makeCuboidShape(5.75, 2.5, 13.75, 7.25, 4.75, 15.25),
            Block.makeCuboidShape(8.75, 11.25, 13.75, 10.25, 13.5, 15.25),
            Block.makeCuboidShape(8.75, 2.5, 13.75, 10.25, 4.75, 15.25),
            Block.makeCuboidShape(9, 3.5, 14, 10, 12.75, 15),
            Block.makeCuboidShape(1.25, 6.25, 7.5, 2.25, 7.25, 8.5),
            Block.makeCuboidShape(13.75, 6.25, 7.5, 14.75, 7.25, 8.5),
            Block.makeCuboidShape(14, 3.5, 7, 15, 7.75, 9),
            Block.makeCuboidShape(1, 3.5, 7, 2, 7.75, 9),
            Block.makeCuboidShape(11, 2, 6, 15, 4, 10),
            Block.makeCuboidShape(1, 2, 6, 5, 4, 10),
            Block.makeCuboidShape(6, 2, 11, 10, 4, 15),
            Block.makeCuboidShape(6, 12, 9, 10, 15, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(4, 2, 4, 12, 3, 12),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(6, 2.5, 6, 10, 3.5, 10),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(5, 6, 5, 11, 12, 11),
            Block.makeCuboidShape(6.75, 2.5, 0.75, 9.25, 4.75, 2.25),
            Block.makeCuboidShape(6.75, 2.5, 13.75, 9.25, 4.75, 15.25),
            Block.makeCuboidShape(1, 3.5, 6, 2, 12.75, 7),
            Block.makeCuboidShape(0.75, 11.25, 5.75, 2.25, 13.5, 7.25),
            Block.makeCuboidShape(0.75, 2.5, 5.75, 2.25, 4.75, 7.25),
            Block.makeCuboidShape(0.75, 11.25, 8.75, 2.25, 13.5, 10.25),
            Block.makeCuboidShape(0.75, 2.5, 8.75, 2.25, 4.75, 10.25),
            Block.makeCuboidShape(1, 3.5, 9, 2, 12.75, 10),
            Block.makeCuboidShape(7.5, 6.25, 1.25, 8.5, 7.25, 2.25),
            Block.makeCuboidShape(7.5, 6.25, 13.75, 8.5, 7.25, 14.75),
            Block.makeCuboidShape(7, 3.5, 14, 9, 7.75, 15),
            Block.makeCuboidShape(7, 3.5, 1, 9, 7.75, 2),
            Block.makeCuboidShape(6, 2, 11, 10, 4, 15),
            Block.makeCuboidShape(6, 2, 1, 10, 4, 5),
            Block.makeCuboidShape(1, 2, 6, 5, 4, 10),
            Block.makeCuboidShape(1, 12, 6, 7, 15, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(4, 2, 4, 12, 3, 12),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(6, 2.5, 6, 10, 3.5, 10),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(5, 6, 5, 11, 12, 11),
            Block.makeCuboidShape(13.75, 2.5, 6.75, 15.25, 4.75, 9.25),
            Block.makeCuboidShape(0.75, 2.5, 6.75, 2.25, 4.75, 9.25),
            Block.makeCuboidShape(9, 3.5, 1, 10, 12.75, 2),
            Block.makeCuboidShape(8.75, 11.25, 0.75, 10.25, 13.5, 2.25),
            Block.makeCuboidShape(8.75, 2.5, 0.75, 10.25, 4.75, 2.25),
            Block.makeCuboidShape(5.75, 11.25, 0.75, 7.25, 13.5, 2.25),
            Block.makeCuboidShape(5.75, 2.5, 0.75, 7.25, 4.75, 2.25),
            Block.makeCuboidShape(6, 3.5, 1, 7, 12.75, 2),
            Block.makeCuboidShape(13.75, 6.25, 7.5, 14.75, 7.25, 8.5),
            Block.makeCuboidShape(1.25, 6.25, 7.5, 2.25, 7.25, 8.5),
            Block.makeCuboidShape(1, 3.5, 7, 2, 7.75, 9),
            Block.makeCuboidShape(14, 3.5, 7, 15, 7.75, 9),
            Block.makeCuboidShape(1, 2, 6, 5, 4, 10),
            Block.makeCuboidShape(11, 2, 6, 15, 4, 10),
            Block.makeCuboidShape(6, 2, 1, 10, 4, 5),
            Block.makeCuboidShape(6, 12, 1, 10, 15, 7)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(4, 2, 4, 12, 3, 12),
            Block.makeCuboidShape(4, 0, 4, 12, 1, 12),
            Block.makeCuboidShape(6, 2.5, 6, 10, 3.5, 10),
            Block.makeCuboidShape(5, 1, 5, 11, 2, 11),
            Block.makeCuboidShape(5, 6, 5, 11, 12, 11),
            Block.makeCuboidShape(6.75, 2.5, 13.75, 9.25, 4.75, 15.25),
            Block.makeCuboidShape(6.75, 2.5, 0.75, 9.25, 4.75, 2.25),
            Block.makeCuboidShape(14, 3.5, 9, 15, 12.75, 10),
            Block.makeCuboidShape(13.75, 11.25, 8.75, 15.25, 13.5, 10.25),
            Block.makeCuboidShape(13.75, 2.5, 8.75, 15.25, 4.75, 10.25),
            Block.makeCuboidShape(13.75, 11.25, 5.75, 15.25, 13.5, 7.25),
            Block.makeCuboidShape(13.75, 2.5, 5.75, 15.25, 4.75, 7.25),
            Block.makeCuboidShape(14, 3.5, 6, 15, 12.75, 7),
            Block.makeCuboidShape(7.5, 6.25, 13.75, 8.5, 7.25, 14.75),
            Block.makeCuboidShape(7.5, 6.25, 1.25, 8.5, 7.25, 2.25),
            Block.makeCuboidShape(7, 3.5, 1, 9, 7.75, 2),
            Block.makeCuboidShape(7, 3.5, 14, 9, 7.75, 15),
            Block.makeCuboidShape(6, 2, 1, 10, 4, 5),
            Block.makeCuboidShape(6, 2, 11, 10, 4, 15),
            Block.makeCuboidShape(11, 2, 6, 15, 4, 10),
            Block.makeCuboidShape(9, 12, 6, 15, 15, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
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
        switch (state.get(FACING)) {
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
