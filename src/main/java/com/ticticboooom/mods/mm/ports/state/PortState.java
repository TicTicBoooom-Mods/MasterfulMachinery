package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import lombok.Setter;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class PortState {
    @Getter
    @Setter
    private boolean consumePerTick = false;

    @Getter
    @Setter
    private double chance = 0;

    @Getter
    @Setter
    private boolean instantConsume = false;
    public abstract void processRequirement(List<PortStorage> storage);
    public abstract boolean validateRequirement(List<PortStorage> storage);
    public abstract void processResult(List<PortStorage> storage);
    public abstract boolean validateResult(List<PortStorage> storage);
    public abstract ResourceLocation getName();
    public void validateDefinition() {}
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {}
    public abstract IIngredientType<?> getJeiIngredientType();
    public <T> List<T> getIngredient(boolean input) {
        return new ArrayList<>();
    }
    public boolean supportsChances() {
        return true;
    }
    public boolean supportsPerTick(){
        return true;
    }
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {

    }
}
