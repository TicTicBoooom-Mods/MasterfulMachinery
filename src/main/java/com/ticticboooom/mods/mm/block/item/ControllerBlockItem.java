package com.ticticboooom.mods.mm.block.item;

import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.util.ControllerHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ControllerBlockItem extends BlockItem {
    public ControllerBlockItem() {
        super(MMBlocks.CONTROLLER.get(), new Properties().group(MMItemGroup.INSTANCE));
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        ResourceLocation loc = ControllerHelper.getId(stack);
        if (loc == null) {
            return false;
        }
        if (!DataRegistry.CONTROLLERS.containsKey(loc)){
            return false;
        }
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ControllerTile) {
            ControllerTile controller = (ControllerTile) tileEntity;
            controller.controllerModel = DataRegistry.CONTROLLERS.get(loc);
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }
}
