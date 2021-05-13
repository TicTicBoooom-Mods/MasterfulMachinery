package com.ticticboooom.mods.mm.client.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.state.FluidPortState;
import com.ticticboooom.mods.mm.ports.state.ItemPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.MekGasPortStorage;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.inputs.chemical.ChemicalIngredientDeserializer;
import mekanism.api.recipes.inputs.chemical.GasStackIngredient;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MachineProcessRecipeCategory implements IRecipeCategory<MachineProcessRecipe> {

    private static final ResourceLocation overlayRl = new ResourceLocation(MM.ID, "textures/gui/gui_large_jei.png");

    private IJeiHelpers helpers;
    private String structureId;

    public MachineProcessRecipeCategory(IJeiHelpers helpers, String structureId) {
        this.helpers = helpers;

        this.structureId = structureId;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(MM.ID, "machine_process_" + structureId);
    }

    @Override
    public Class<? extends MachineProcessRecipe> getRecipeClass() {
        return MachineProcessRecipe.class;
    }

    @Override
    public String getTitle() {
        return "Process";
    }

    public IDrawable getBackground() {
        return helpers.getGuiHelper().createBlankDrawable(162, 150);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(MachineProcessRecipe recipe, IIngredients ingredients) {
        for (PortState input : recipe.getInputs()) {
            input.setIngredient(ingredients, true);
        }
        for (PortState output : recipe.getOutputs()) {
            output.setIngredient(ingredients, false);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MachineProcessRecipe recipe, IIngredients ingredients) {
        final int inputWidth = 60;
        Map<String, Integer> indexMap = new HashMap<>();
        int currentX = 0;
        int currentY = 0;
        for (PortState input : recipe.getInputs()) {
            Integer index = indexMap.getOrDefault(input.getClass().toString(), 0);
            input.setupRecipe(recipeLayout, index, currentX, currentY, true);
            indexMap.put(input.getClass().toString(), ++index);
            currentX += 20;
            if (currentX >= inputWidth) {
                currentX = 0;
                currentY += 20;
            }
        }


        final int offsetX = 95;
        currentX = offsetX;
        currentY = 0;
        for (PortState output : recipe.getOutputs()) {
            Integer index = indexMap.getOrDefault(output.getClass().toString(), 0);
            output.setupRecipe(recipeLayout, index, currentX, currentY, false);
            indexMap.put(output.getClass().toString(), ++index);
            currentX += 20;
            if (currentX >= inputWidth + offsetX) {
                currentX = offsetX;
                currentY += 20;
            }
        }
    }

    @Override
    public void draw(MachineProcessRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        final int inputWidth = 60;

        int currentX = 0;
        int currentY = 0;
        for (PortState input : recipe.getInputs()) {
            input.render(matrixStack, currentX, currentY, (int) mouseX, (int) mouseY, helpers);
            currentX += 20;
            if (currentX >= inputWidth) {
                currentX = 0;
                currentY += 20;
            }
        }

        final int offsetX = 95;
        currentX = offsetX;
        currentY = 0;
        for (PortState output : recipe.getOutputs()) {
            output.render(matrixStack, currentX, currentY, (int) mouseX, (int) mouseY, helpers);
            currentX += 20;
            if (currentX >= inputWidth + offsetX) {
                currentX = offsetX;
                currentY += 20;
            }
        }

        IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 26, 0, 24, 17);
        drawable.draw(matrixStack, 66, 5);
    }
}
