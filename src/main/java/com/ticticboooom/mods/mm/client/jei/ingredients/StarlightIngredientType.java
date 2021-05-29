package com.ticticboooom.mods.mm.client.jei.ingredients;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import mezz.jei.api.ingredients.IIngredientType;

public class StarlightIngredientType implements IIngredientType<StarlightStack> {
    @Override
    public Class<? extends StarlightStack> getIngredientClass() {
        return StarlightStack.class;
    }
}
