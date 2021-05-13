package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.Getter;
import lombok.Setter;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class PortState {
    @Getter
    @Setter
    private boolean consumePerTick = false;

    public abstract void processRequirement(List<IPortStorage> storage);
    public abstract boolean validateRequirement(List<IPortStorage> storage);
    public abstract void processResult(List<IPortStorage> storage);
    public abstract boolean validateResult(List<IPortStorage> storage);
    public abstract ResourceLocation getName();
    public void validateDefinition() {}
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {}
    public void setIngredient(IIngredients in, boolean input){

    }

    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {

    }
}
