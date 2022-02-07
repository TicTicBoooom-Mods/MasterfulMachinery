package com.ticticboooom.mods.mm.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class TagHelper {
    public static ResourceLocation getControllerId(ItemStack stack) {
        return getRL(stack, "Controller");
    }

    public static ResourceLocation getPortId(ItemStack stack) {
        return getRL(stack, "Port");
    }

    public static ResourceLocation getRL(ItemStack stack, String key) {
        if (!stack.hasTag()) {
            return null;
        }

        CompoundNBT nbt = stack.getTag();
        if (!nbt.contains(key)) {
            return null;
        }

        String controller = nbt.getString(key);
        return ResourceLocation.tryCreate(controller);
    }


}
