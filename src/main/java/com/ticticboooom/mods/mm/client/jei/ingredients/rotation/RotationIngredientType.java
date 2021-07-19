package com.ticticboooom.mods.mm.client.jei.ingredients.rotation;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.RotationStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import mezz.jei.api.ingredients.IIngredientType;

public class RotationIngredientType implements IIngredientType<RotationStack> {
    @Override
    public Class<? extends RotationStack> getIngredientClass() {
        return RotationStack.class;
    }
}
