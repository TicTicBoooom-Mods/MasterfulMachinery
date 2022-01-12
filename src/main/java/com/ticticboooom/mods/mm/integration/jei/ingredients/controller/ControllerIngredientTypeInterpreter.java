package com.ticticboooom.mods.mm.integration.jei.ingredients.controller;

import com.ticticboooom.mods.mm.block.item.ControllerBlockItem;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.util.ControllerHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ControllerIngredientTypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        ResourceLocation id = ControllerHelper.getId(ingredient);
        if (id == null) {
            return NONE;
        }
        return id.toString();
    }
}
