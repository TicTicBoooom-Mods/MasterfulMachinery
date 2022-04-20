package com.ticticboooom.mods.mm.block.item;

import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControllerBlockItem extends BlockItem {
    public ControllerBlockItem() {
        super(MMBlocks.CONTROLLER.get(), new Properties().group(MMItemGroup.INSTANCE));
    }


    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof ControllerTile) {
            ControllerTile controller = (ControllerTile) tileEntity;
            controller.controllerModel = getModel(stack);
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ControllerModel model = getModel(stack);
        if (model == null) {
            return super.getDisplayName(stack);
        }
        return model.name;
    }

    private ControllerModel getModel(ItemStack stack) {
        ResourceLocation loc = TagHelper.getControllerId(stack);
        if (loc == null) {
            return null;
        }
        if (!DataRegistry.CONTROLLERS.containsKey(loc)) {
            return null;
        }
        return DataRegistry.CONTROLLERS.get(loc);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        Set<Map.Entry<ResourceLocation, ControllerModel>> entries = DataRegistry.CONTROLLERS.entrySet();
        if (entries.size() != 0) {
            Map.Entry<ResourceLocation, ControllerModel> next = entries.iterator().next();
            stack.getOrCreateTag().putString("Controller", next.getValue().id.toString());
        }
        super.onCreated(stack, worldIn, playerIn);
    }


}
