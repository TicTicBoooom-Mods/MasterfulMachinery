package com.ticticboooom.mods.mm.integration.jei.ingredients.controller;

import com.ticticboooom.mods.mm.util.TagHelper;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ControllerIngredientTypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        ResourceLocation id = TagHelper.getControllerId(ingredient);
        if (id == null) {
            return NONE;
        }
        return id.toString();
    }
}
