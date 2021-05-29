package com.ticticboooom.mods.mm.client.jei.ingredients;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

public class StarlightIngredientHelper implements IIngredientHelper<StarlightStack> {

    @Nullable
    @Override
    public StarlightStack getMatch(Iterable<StarlightStack> ingredients, StarlightStack ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(StarlightStack ingredient) {
        return "Starlight";
    }

    @Override
    public String getUniqueId(StarlightStack ingredient) {
        return ingredient.getAmount() + "";
    }

    @Override
    public String getModId(StarlightStack ingredient) {
        return "astralsorcery";
    }

    @Override
    public String getResourceId(StarlightStack ingredient) {
        return "starlight";
    }

    @Override
    public StarlightStack copyIngredient(StarlightStack ingredient) {
        return new StarlightStack(ingredient.getAmount());
    }

    @Override
    public String getErrorInfo(@Nullable StarlightStack ingredient) {
        return "Error";
    }
}
