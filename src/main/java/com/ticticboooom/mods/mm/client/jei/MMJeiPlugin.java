package com.ticticboooom.mods.mm.client.jei;

import com.google.common.collect.ImmutableList;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.client.jei.category.MachineProcessRecipeCategory;
import com.ticticboooom.mods.mm.client.jei.category.MachineStructureRecipeCategory;
import com.ticticboooom.mods.mm.client.jei.ingredients.*;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class MMJeiPlugin implements IModPlugin {
    public static final EnergyIngredientType ENERGY_TYPE = new EnergyIngredientType();
    public static final EnergyIngredientRenderer ENERGY_TYPE_RENDERER = new EnergyIngredientRenderer();
    public static final EnergyIngredientHelper ENERGY_TYPE_HELPER = new EnergyIngredientHelper();

    public static final PNCPressureIngredientType PRESSURE_TYPE = new PNCPressureIngredientType();
    public static final PNCPressureIngredientRenderer PRESSURE_TYPE_RENDERER = new PNCPressureIngredientRenderer();
    public static final PNCPressureIngredientHelper PRESSURE_TYPE_HELPER = new PNCPressureIngredientHelper();

    public static final ManaIngredientType MANA_TYPE = new ManaIngredientType();
    public static final ManaIngredientRenderer MANA_TYPE_RENDERER = new ManaIngredientRenderer();
    public static final ManaIngredientHelper MANA_TYPE_HELPER = new ManaIngredientHelper();

    public static final StarlightIngredientType STAR_TYPE = new StarlightIngredientType();
    public static final StarlightIngredientRenderer STAR_TYPE_RENDERER = new StarlightIngredientRenderer();
    public static final StarlightIngredientHelper STAR_TYPE_HELPER = new StarlightIngredientHelper();

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MM.ID, "jei_main");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        registration.register(MMJeiPlugin.ENERGY_TYPE, ImmutableList.of(), ENERGY_TYPE_HELPER, ENERGY_TYPE_RENDERER);
        registration.register(MMJeiPlugin.PRESSURE_TYPE, ImmutableList.of(), PRESSURE_TYPE_HELPER, PRESSURE_TYPE_RENDERER);
        registration.register(MMJeiPlugin.MANA_TYPE, ImmutableList.of(), MANA_TYPE_HELPER, MANA_TYPE_RENDERER);
        registration.register(MMJeiPlugin.STAR_TYPE, ImmutableList.of(), STAR_TYPE_HELPER, STAR_TYPE_RENDERER);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ENERGY_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        PRESSURE_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        MANA_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        STAR_TYPE_RENDERER.setHelpers(registration.getJeiHelpers());
        List<MachineStructureRecipe> structureRecipes = Minecraft.getInstance().world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_STRUCTURE);
        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            registration.addRecipes(structureRecipes.stream().filter(x -> x.getControllerId().contains(block.get().getControllerId())).collect(Collectors.toList()), new ResourceLocation(MM.ID, "machine_structure_" + block.get().getControllerId()));
        }

        List<MachineProcessRecipe> processRecipes = Minecraft.getInstance().world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_PROCESS);
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
        List<MachineStructureRecipe> structureRecipes = Minecraft.getInstance().world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_STRUCTURE);
        for (MachineStructureRecipe structureRecipe : structureRecipes) {
            registration.addRecipeCategories(new MachineProcessRecipeCategory(registration.getJeiHelpers(), structureRecipe.getStructureId(), structureRecipe.getName()));
        }
    }
}
