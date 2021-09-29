package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class StructureGenBlock extends Block {
    private static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.makeCuboidShape(1, 2, 1, 15, 5, 6),
            Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
            Block.makeCuboidShape(0.5, 2, 6.5, 15.5, 5, 15.5),
            Block.makeCuboidShape(0, 5, 6, 16, 6, 16),
            Block.makeCuboidShape(4, 6, 7, 15, 14, 13),
            Block.makeCuboidShape(1, 5.5, 7, 3, 6.5, 9),
            Block.makeCuboidShape(1.25, 11, 7.25, 2.75, 11.25, 8.75),
            Block.makeCuboidShape(1, 6, 11, 3, 12.25, 14),
            Block.makeCuboidShape(1, 11.25, 6.75, 3, 12.25, 11.25),
            Block.makeCuboidShape(0, 2, 0, 1, 3, 6),
            Block.makeCuboidShape(15, 2, 0, 16, 3, 6),
            Block.makeCuboidShape(0, 2, 6, 1, 5, 7),
            Block.makeCuboidShape(15, 2, 6, 16, 5, 7),
            Block.makeCuboidShape(15, 2, 15, 16, 5, 16),
            Block.makeCuboidShape(0, 2, 15, 1, 5, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.makeCuboidShape(10, 2, 1, 15, 5, 15),
            Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
            Block.makeCuboidShape(0.5, 2, 0.5, 9.5, 5, 15.5),
            Block.makeCuboidShape(0, 5, 0, 10, 6, 16),
            Block.makeCuboidShape(3, 6, 4, 9, 14, 15),
            Block.makeCuboidShape(7, 5.5, 1, 9, 6.5, 3),
            Block.makeCuboidShape(7.25, 11, 1.25, 8.75, 11.25, 2.75),
            Block.makeCuboidShape(2, 6, 1, 5, 12.25, 3),
            Block.makeCuboidShape(4.75, 11.25, 1, 9.25, 12.25, 3),
            Block.makeCuboidShape(10, 2, 0, 16, 3, 1),
            Block.makeCuboidShape(10, 2, 15, 16, 3, 16),
            Block.makeCuboidShape(9, 2, 0, 10, 5, 1),
            Block.makeCuboidShape(9, 2, 15, 10, 5, 16),
            Block.makeCuboidShape(0, 2, 15, 1, 5, 16),
            Block.makeCuboidShape(0, 2, 0, 1, 5, 1)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.makeCuboidShape(1, 2, 10, 15, 5, 15),
            Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
            Block.makeCuboidShape(0.5, 2, 0.5, 15.5, 5, 9.5),
            Block.makeCuboidShape(0, 5, 0, 16, 6, 10),
            Block.makeCuboidShape(1, 6, 3, 12, 14, 9),
            Block.makeCuboidShape(13, 5.5, 7, 15, 6.5, 9),
            Block.makeCuboidShape(13.25, 11, 7.25, 14.75, 11.25, 8.75),
            Block.makeCuboidShape(13, 6, 2, 15, 12.25, 5),
            Block.makeCuboidShape(13, 11.25, 4.75, 15, 12.25, 9.25),
            Block.makeCuboidShape(15, 2, 10, 16, 3, 16),
            Block.makeCuboidShape(0, 2, 10, 1, 3, 16),
            Block.makeCuboidShape(15, 2, 9, 16, 5, 10),
            Block.makeCuboidShape(0, 2, 9, 1, 5, 10),
            Block.makeCuboidShape(0, 2, 0, 1, 5, 1),
            Block.makeCuboidShape(15, 2, 0, 16, 5, 1)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.makeCuboidShape(1, 2, 1, 6, 5, 15),
            Block.makeCuboidShape(0, 0, 0, 16, 2, 16),
            Block.makeCuboidShape(6.5, 2, 0.5, 15.5, 5, 15.5),
            Block.makeCuboidShape(6, 5, 0, 16, 6, 16),
            Block.makeCuboidShape(7, 6, 1, 13, 14, 12),
            Block.makeCuboidShape(7, 5.5, 13, 9, 6.5, 15),
            Block.makeCuboidShape(7.25, 11, 13.25, 8.75, 11.25, 14.75),
            Block.makeCuboidShape(11, 6, 13, 14, 12.25, 15),
            Block.makeCuboidShape(6.75, 11.25, 13, 11.25, 12.25, 15),
            Block.makeCuboidShape(0, 2, 15, 6, 3, 16),
            Block.makeCuboidShape(0, 2, 0, 6, 3, 1),
            Block.makeCuboidShape(6, 2, 15, 7, 5, 16),
            Block.makeCuboidShape(6, 2, 0, 7, 5, 1),
            Block.makeCuboidShape(15, 2, 0, 16, 5, 1),
            Block.makeCuboidShape(15, 2, 15, 16, 5, 16)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public StructureGenBlock() {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(0)
            .harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(FACING));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (!level.isRemote()) {
            TileEntity blockEntity = level.getTileEntity(pos);
            if (blockEntity instanceof StructureGenBlockEntity) {
                NetworkHooks.openGui(((ServerPlayerEntity) player), (StructureGenBlockEntity)blockEntity, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof StructureGenBlockEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, ((StructureGenBlockEntity) tileentity).getInv());
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
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
