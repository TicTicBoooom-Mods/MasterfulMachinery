package com.ticticboooom.mods.mm.item;

import com.ticticboooom.mods.mm.block.item.MMItemGroup;
import com.ticticboooom.mods.mm.client.container.BlueprintContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlueprintItem extends Item {
    public BlueprintItem() {
        super(new Item.Properties().group(MMItemGroup.INSTANCE));
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            NetworkHooks.openGui((ServerPlayerEntity) playerIn,  new BlueprintContainerProvider());
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

}
