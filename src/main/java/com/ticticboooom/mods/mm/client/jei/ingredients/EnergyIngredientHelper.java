package com.ticticboooom.mods.mm.client.jei.ingredients;

import com.ticticboooom.mods.mm.client.jei.ingredients.model.EnergyStack;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<EnergyStack> {
    @Nullable
    @Override
    public EnergyStack getMatch(Iterable<EnergyStack> ingredients, EnergyStack ingredientToMatch) {
        return ingredientToMatch;
    }

    @Override
    public String getDisplayName(EnergyStack ingredient) {
        return "Forge Energy";
    }

    @Override
    public String getUniqueId(EnergyStack ingredient) {
        return "fe_" + ingredient + "_fe";
    }

    @Override
    public String getModId(EnergyStack ingredient) {
        return "forge";
    }

    @Override
    public String getResourceId(EnergyStack ingredient) {
        return "fe_" + ingredient.getAmount() + "_fe";
    }

    @Override
    public EnergyStack copyIngredient(EnergyStack ingredient) {
        return new EnergyStack(ingredient.getAmount());
    }

    @Override
    public String getErrorInfo(@Nullable EnergyStack ingredient) {
        return "Error";
    }
}
