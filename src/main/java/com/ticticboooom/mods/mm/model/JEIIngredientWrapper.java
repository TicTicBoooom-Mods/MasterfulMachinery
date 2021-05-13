package com.ticticboooom.mods.mm.model;

import lombok.Getter;
import mezz.jei.api.ingredients.IIngredientType;

import java.util.ArrayList;
import java.util.List;

public class JEIIngredientWrapper<TStack> {
    @Getter
    List<TStack> stacks = new ArrayList<>();

    public void add(IIngredientType<TStack> type, TStack stack) {
        stacks.add(stack);
    }

    public void add(IIngredientType<TStack> type, List<TStack> stack) {
        stacks.addAll(stack);
    }
}
