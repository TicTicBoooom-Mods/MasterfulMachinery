package com.ticticboooom.mods.mm.client.jei.category;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class MMJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MM.ID, "jei_main");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<MachineStructureRecipe> structureRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes
                .MACHINE_STRUCTURE);
        registration.addRecipes(structureRecipes, new ResourceLocation(MM.ID, "machine_structure"));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MachineStructureRecipeCategory(registration.getJeiHelpers()));
    }
}
