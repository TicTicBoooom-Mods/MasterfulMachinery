package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import lombok.Getter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
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

    public MachinePortBlock(RegistryObject<TileEntityType<?>> type, String name, String controllerId, String textureOverride) {
        super(AbstractBlock.Properties.of(Material.METAL));
        this.type = type;
        this.langName = name;
        this.controllerId = controllerId;
        this.textureOverride = textureOverride;
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
    public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (!level.isClientSide()) {
            TileEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MachinePortBlockEntity) {
                NetworkHooks.openGui(((ServerPlayerEntity) player), (MachinePortBlockEntity)blockEntity, pos);
            }
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState state1, boolean p_196243_5_) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof MachinePortBlockEntity){
            Object o = ((MachinePortBlockEntity) tile).getStorage().getLO().orElse(null);
            if (o instanceof ItemStackHandler) {
                InventoryHelper.dropContents(world, pos, new ItemStackInventory((ItemStackHandler) o));
            }
        }
        super.onRemove(state, world, pos, state1, p_196243_5_);
    }
}
