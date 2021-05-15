package com.ticticboooom.mods.mm.client.jei.category;

import com.google.common.collect.ImmutableList;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.client.jei.ingredients.EnergyIngredientHelper;
import com.ticticboooom.mods.mm.client.jei.ingredients.EnergyIngredientRenderer;
import com.ticticboooom.mods.mm.client.jei.ingredients.EnergyIngredientType;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class MMJeiPlugin implements IModPlugin {
    public static final EnergyIngredientType ENERGY_TYPE = new EnergyIngredientType();
    public static final EnergyIngredientRenderer ENERGY_TYPE_RENDERER = new EnergyIngredientRenderer();
    public static final EnergyIngredientHelper ENERGY_TYPE_HELPER = new EnergyIngredientHelper();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MM.ID, "jei_main");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(MMJeiPlugin.ENERGY_TYPE, ImmutableList.of(), ENERGY_TYPE_HELPER, ENERGY_TYPE_RENDERER);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ENERGY_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        List<MachineStructureRecipe> structureRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes.MACHINE_STRUCTURE);
        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            registration.addRecipes(structureRecipes.stream().filter(x -> x.getControllerId().contains(block.get().getControllerId())).collect(Collectors.toList()), new ResourceLocation(MM.ID, "machine_structure_" + block.get().getControllerId()));
        }

        List<MachineProcessRecipe> processRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes.MACHINE_PROCESS);
        for (MachineStructureRecipe structureRecipe : structureRecipes) {
            List<MachineProcessRecipe> recipes = processRecipes.stream().filter(x -> x.getStructureId().equals(structureRecipe.getStructureId())).collect(Collectors.toList());
            registration.addRecipes(recipes, new ResourceLocation(MM.ID, "machine_process_" + structureRecipe.getStructureId()));
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        ENERGY_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            registration.addRecipeCategories(new MachineStructureRecipeCategory(registration.getJeiHelpers(), block.get()));
        }
        List<MachineStructureRecipe> structureRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeTypes.MACHINE_STRUCTURE);
        for (MachineStructureRecipe structureRecipe : structureRecipes) {
            registration.addRecipeCategories(new MachineProcessRecipeCategory(registration.getJeiHelpers(), structureRecipe.getStructureId(), structureRecipe.getName()));
        }
    }
}
