package com.ticticboooom.mods.mm.client.jei.ingredients.rotation;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.RotationStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

public class RotationIngredientHelper implements IIngredientHelper<RotationStack> {

    @Nullable
    @Override
    public RotationStack getMatch(Iterable<RotationStack> ingredients, RotationStack ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(RotationStack ingredient) {
        return "SU";
    }

    @Override
    public String getUniqueId(RotationStack ingredient) {
        return ingredient.getSpeed() + "";
    }

    @Override
    public String getModId(RotationStack ingredient) {
        return "create";
    }

    @Override
    public String getResourceId(RotationStack ingredient) {
        return "rotation";
    }

    @Override
    public RotationStack copyIngredient(RotationStack ingredient) {
        return new RotationStack(ingredient.getSpeed());
    }

    @Override
    public String getErrorInfo(@Nullable RotationStack ingredient) {
        return "Error";
    }
}
