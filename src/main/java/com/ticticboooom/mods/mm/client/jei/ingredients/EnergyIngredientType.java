package com.ticticboooom.mods.mm.client.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientType;

public class EnergyIngredientType implements IIngredientType<Integer> {
    @Override
    public Class<? extends Integer> getIngredientClass() {
        return Integer.class;
    }
}
