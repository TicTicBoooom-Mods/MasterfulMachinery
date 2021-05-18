package com.ticticboooom.mods.mm.item;

import com.ticticboooom.mods.mm.helper.NBTHelper;
import com.ticticboooom.mods.mm.registration.MMLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureGenSelectionDevice extends Item {
    public StructureGenSelectionDevice() {
        super(new Item.Properties().group(MMLoader.MASTERFUL_ITEM_GROUP));
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        BlockPos clickedPos = ctx.getPos();
        ItemStack iih = ctx.getItem();
        CompoundNBT tag = iih.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
        }
        if (!tag.contains("pos1") && !tag.contains("pos2")) {
            tag.put("pos1", NBTHelper.toCompound(clickedPos));
        } else if (tag.contains("pos1") && !tag.contains("pos2")) {
            tag.put("pos2", NBTHelper.toCompound(clickedPos));
        } else {
            if (tag.contains("pos2")) {
                tag.remove("pos2");
            }
            tag.put("pos1", NBTHelper.toCompound(clickedPos));
        }

        return ActionResultType.SUCCESS;
    }
}
