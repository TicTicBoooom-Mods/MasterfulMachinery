package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import lombok.Getter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class MachinePortBlock extends Block {
    private RegistryObject<TileEntityType<?>> type;
    @Getter
    private String langName;
    @Getter
    private String controllerId;
    @Getter
    private String textureOverride;
    @Getter
    private ResourceLocation overlay;
    @Getter
    private ResourceLocation portTypeId;

    public MachinePortBlock(RegistryObject<TileEntityType<?>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay, ResourceLocation portTypeId) {
        super(AbstractBlock.Properties.create(Material.IRON).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(0)
                .harvestTool(ToolType.PICKAXE));
        this.type = type;
        this.langName = name;
        this.controllerId = controllerId;
        this.textureOverride = textureOverride;
        this.overlay = overlay;
        this.portTypeId = portTypeId;
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
    public ActionResultType onBlockActivated(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (!level.isRemote()) {
            TileEntity blockEntity = level.getTileEntity(pos);
            if (blockEntity instanceof IMachinePortTile) {
                IMachinePortTile port = (IMachinePortTile) blockEntity;
                if (!port.getStorage().onPortActivated(state, level, pos, player, hand ,traceResult)) {
                    NetworkHooks.openGui(((ServerPlayerEntity) player), port, pos);
                }
            }
        }

        return ActionResultType.SUCCESS;
    }


    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tile = worldIn.getTileEntity(pos);
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        if (tile instanceof MachinePortBlockEntity){
            LazyOptional<Object> lo = ((MachinePortBlockEntity) tile).getStorage().getLO();
            if (lo == null){
                return;
            }
            Object o = lo.orElse(null);
            if (o instanceof ItemStackHandler) {
                InventoryHelper.dropInventoryItems(worldIn, pos, new ItemStackInventory((ItemStackHandler) o));
            }
        }
        tile.remove();
    }


    @Override
    public void neighborChanged(BlockState p_220069_1_, World world, BlockPos pos, Block p_220069_4_, BlockPos changedPos, boolean p_220069_6_) {
        super.neighborChanged(p_220069_1_, world, pos, p_220069_4_, changedPos, p_220069_6_);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IMachinePortTile){
            ((IMachinePortTile) tile).getStorage().neighborChanged();
        }
    }
}
