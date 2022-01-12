package com.ticticboooom.mods.mm.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ControllerHelper {
    public static ResourceLocation getId(ItemStack stack) {
        if (!stack.hasTag()) {
            return null;
        }

        CompoundNBT nbt = stack.getTag();
        if (!nbt.contains("Controller")) {
            return null;
        }

        String controller = nbt.getString("Controller");
        return ResourceLocation.tryCreate(controller);
    }
}
