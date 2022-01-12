package com.ticticboooom.mods.mm.integration.jei.ingredients.controller;

import com.ticticboooom.mods.mm.data.model.ControllerModel;
import mezz.jei.api.ingredients.IIngredientType;

public class ControllerIngredientType implements IIngredientType<ControllerModel> {
    @Override
    public Class<? extends ControllerModel> getIngredientClass() {
        return ControllerModel.class;
    }
}
