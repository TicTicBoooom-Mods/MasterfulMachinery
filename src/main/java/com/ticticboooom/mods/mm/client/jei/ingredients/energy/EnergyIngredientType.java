package com.ticticboooom.mods.mm.client.jei.ingredients.energy;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.EnergyStack;
import mezz.jei.api.ingredients.IIngredientType;

public class EnergyIngredientType implements IIngredientType<EnergyStack> {
    @Override
    public Class<? extends EnergyStack> getIngredientClass() {
        return EnergyStack.class;
    }
}
