package com.ticticboooom.mods.mm.client.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<Integer> {
    @Nullable
    @Override
    public Integer getMatch(Iterable<Integer> ingredients, Integer ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(Integer ingredient) {
        return "Forge Energy";
    }

    @Override
    public String getUniqueId(Integer ingredient) {
        return ingredient + "";
    }

    @Override
    public String getModId(Integer ingredient) {
        return "forge";
    }

    @Override
    public String getResourceId(Integer ingredient) {
        return "";
    }

    @Override
    public Integer copyIngredient(Integer ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable Integer ingredient) {
        return "Error";
    }
}
