package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import lombok.Getter;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ControllerBlock extends HorizontalBlock {
    private final RegistryObject<TileEntityType<?>> type;
    @Getter
    private final String controllerName;
    @Getter
    private final String controllerId;
    @Getter
    private final String texOverride;

    public ControllerBlock(RegistryObject<TileEntityType<?>> type, String name, String id, String texOverride) {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(0)
                .harvestTool(ToolType.PICKAXE));
        this.type = type;
        this.controllerName = name;
        this.controllerId = id;
        this.texOverride = texOverride;
        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(HORIZONTAL_FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return type.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote()) {
            TileEntity blockEntity = worldIn.getTileEntity(pos);
            if (blockEntity instanceof ControllerBlockEntity) {
                NetworkHooks.openGui(((ServerPlayerEntity) player), (ControllerBlockEntity)blockEntity, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!worldIn.isRemote()) {
            TileEntity blockEntity = worldIn.getTileEntity(pos);
            if (blockEntity instanceof ControllerBlockEntity) {
                // Send an update to ports notifying that the current recipe was interrupted
                ControllerBlockEntity controller = (ControllerBlockEntity) blockEntity;
                controller.invalidateRecipe();
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
