package com.ticticboooom.mods.mm.integration.jei.ingredients.controller;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

public class ControllerIngredientHelper implements IIngredientHelper<ControllerModel> {
    @Nullable
    @Override
    public ControllerModel getMatch(Iterable<ControllerModel> ingredients, ControllerModel ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(ControllerModel ingredient) {
        return "MM Controller";
    }

    @Override
    public String getUniqueId(ControllerModel ingredient) {
        return ingredient.id.toString();
    }

    @Override
    public String getModId(ControllerModel ingredient) {
        return Ref.MOD_ID;
    }

    @Override
    public String getResourceId(ControllerModel ingredient) {
        return ingredient.id.getPath();
    }

    @Override
    public ControllerModel copyIngredient(ControllerModel ingredient) {
        return ingredient.copy();
    }

    @Override
    public String getErrorInfo(@Nullable ControllerModel ingredient) {
        return "Error";
    }
}
