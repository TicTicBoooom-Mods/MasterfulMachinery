package com.ticticboooom.mods.mm.client.jei.ingredients.as;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import mezz.jei.api.ingredients.IIngredientType;

public class StarlightIngredientType implements IIngredientType<StarlightStack> {
    @Override
    public Class<? extends StarlightStack> getIngredientClass() {
        return StarlightStack.class;
    }
}
