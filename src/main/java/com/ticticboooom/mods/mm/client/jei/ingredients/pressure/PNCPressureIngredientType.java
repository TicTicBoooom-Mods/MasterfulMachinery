package com.ticticboooom.mods.mm.client.jei.ingredients.pressure;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import mezz.jei.api.ingredients.IIngredientType;

public class PNCPressureIngredientType implements IIngredientType<PressureStack> {
    @Override
    public Class<? extends PressureStack> getIngredientClass() {
        return PressureStack.class;
    }
}
