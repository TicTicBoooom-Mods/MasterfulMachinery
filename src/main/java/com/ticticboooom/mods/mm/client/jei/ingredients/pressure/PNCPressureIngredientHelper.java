package com.ticticboooom.mods.mm.client.jei.ingredients.pressure;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class PNCPressureIngredientHelper implements IIngredientHelper<PressureStack> {

    @Nullable
    @Override
    public PressureStack getMatch(Iterable<PressureStack> ingredients, PressureStack ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(PressureStack ingredient) {
        return "Pressure";
    }

    @Override
    public String getUniqueId(PressureStack ingredient) {
        return ingredient.getAmount() + "";
    }

    @Override
    public String getModId(PressureStack ingredient) {
        return "pneumaticcraft";
    }

    @Override
    public String getResourceId(PressureStack ingredient) {
        return "pressure";
    }

    @Override
    public PressureStack copyIngredient(PressureStack ingredient) {
        return new PressureStack(ingredient.getAmount());
    }

    @Override
    public String getErrorInfo(@Nullable PressureStack ingredient) {
        return "Error";
    }
}
