package com.ticticboooom.mods.mm.block.item;

import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PortBlockItem extends BlockItem {
    public PortBlockItem() {
        super(MMBlocks.PORT.get(), new Properties().group(MMItemGroup.INSTANCE));
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof PortTile) {
            PortTile controller = (PortTile) tileEntity;
            controller.portModel = getModel(stack);
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }


    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        PortModel model = getModel(stack);
        if (model == null) {
            return super.getDisplayName(stack);
        }
        return model.name;
    }

    private PortModel getModel(ItemStack stack) {
        ResourceLocation loc = TagHelper.getPortId(stack);
        if (loc == null) {
            return null;
        }
        if (!DataRegistry.PORTS.containsKey(loc)){
            return null;
        }
        return DataRegistry.PORTS.get(loc);
    }
}
