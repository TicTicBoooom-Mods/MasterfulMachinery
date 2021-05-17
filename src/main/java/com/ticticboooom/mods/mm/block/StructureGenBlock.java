package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class StructureGenBlock extends Block {
    private static final DirectionProperty FACING = HorizontalBlock.FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.box(1, 2, 1, 15, 5, 6),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0.5, 2, 6.5, 15.5, 5, 15.5),
            Block.box(0, 5, 6, 16, 6, 16),
            Block.box(4, 6, 7, 15, 14, 13),
            Block.box(1, 5.5, 7, 3, 6.5, 9),
            Block.box(1.25, 11, 7.25, 2.75, 11.25, 8.75),
            Block.box(1, 6, 11, 3, 12.25, 14),
            Block.box(1, 11.25, 6.75, 3, 12.25, 11.25),
            Block.box(0, 2, 0, 1, 3, 6),
            Block.box(15, 2, 0, 16, 3, 6),
            Block.box(0, 2, 6, 1, 5, 7),
            Block.box(15, 2, 6, 16, 5, 7),
            Block.box(15, 2, 15, 16, 5, 16),
            Block.box(0, 2, 15, 1, 5, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.box(10, 2, 1, 15, 5, 15),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0.5, 2, 0.5, 9.5, 5, 15.5),
            Block.box(0, 5, 0, 10, 6, 16),
            Block.box(3, 6, 4, 9, 14, 15),
            Block.box(7, 5.5, 1, 9, 6.5, 3),
            Block.box(7.25, 11, 1.25, 8.75, 11.25, 2.75),
            Block.box(2, 6, 1, 5, 12.25, 3),
            Block.box(4.75, 11.25, 1, 9.25, 12.25, 3),
            Block.box(10, 2, 0, 16, 3, 1),
            Block.box(10, 2, 15, 16, 3, 16),
            Block.box(9, 2, 0, 10, 5, 1),
            Block.box(9, 2, 15, 10, 5, 16),
            Block.box(0, 2, 15, 1, 5, 16),
            Block.box(0, 2, 0, 1, 5, 1)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.box(1, 2, 10, 15, 5, 15),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0.5, 2, 0.5, 15.5, 5, 9.5),
            Block.box(0, 5, 0, 16, 6, 10),
            Block.box(1, 6, 3, 12, 14, 9),
            Block.box(13, 5.5, 7, 15, 6.5, 9),
            Block.box(13.25, 11, 7.25, 14.75, 11.25, 8.75),
            Block.box(13, 6, 2, 15, 12.25, 5),
            Block.box(13, 11.25, 4.75, 15, 12.25, 9.25),
            Block.box(15, 2, 10, 16, 3, 16),
            Block.box(0, 2, 10, 1, 3, 16),
            Block.box(15, 2, 9, 16, 5, 10),
            Block.box(0, 2, 9, 1, 5, 10),
            Block.box(0, 2, 0, 1, 5, 1),
            Block.box(15, 2, 0, 16, 5, 1)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.box(1, 2, 1, 6, 5, 15),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(6.5, 2, 0.5, 15.5, 5, 15.5),
            Block.box(6, 5, 0, 16, 6, 16),
            Block.box(7, 6, 1, 13, 14, 12),
            Block.box(7, 5.5, 13, 9, 6.5, 15),
            Block.box(7.25, 11, 13.25, 8.75, 11.25, 14.75),
            Block.box(11, 6, 13, 14, 12.25, 15),
            Block.box(6.75, 11.25, 13, 11.25, 12.25, 15),
            Block.box(0, 2, 15, 6, 3, 16),
            Block.box(0, 2, 0, 6, 3, 1),
            Block.box(6, 2, 15, 7, 5, 16),
            Block.box(6, 2, 0, 7, 5, 1),
            Block.box(15, 2, 0, 16, 5, 1),
            Block.box(15, 2, 15, 16, 5, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();

    public StructureGenBlock() {
        super(Properties.of(Material.METAL));
    }

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
