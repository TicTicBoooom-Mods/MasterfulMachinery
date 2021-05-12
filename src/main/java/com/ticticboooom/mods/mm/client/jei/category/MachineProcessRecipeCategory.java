package com.ticticboooom.mods.mm.client.jei.category;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class MachineProcessRecipeCategory implements IRecipeCategory<MachineProcessRecipe> {

    private static final ResourceLocation overlayRl = new ResourceLocation(MM.ID, "textures/gui/gui_large_jei.png");

    private IJeiHelpers helpers;
    private String title;

    public MachineProcessRecipeCategory(IJeiHelpers helpers, String title) {
        this.helpers = helpers;

        this.title = title;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(MM.ID, "machine_process");
    }

    @Override
    public Class<? extends MachineProcessRecipe> getRecipeClass() {
        return MachineProcessRecipe.class;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public IDrawable getBackground() {
        return helpers.getGuiHelper().createDrawable(overlayRl, 0, 0, 162, 150);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(MachineProcessRecipe recipe, IIngredients ingredients) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MachineProcessRecipe recipe, IIngredients ingredients) {

    }
}
