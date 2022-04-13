package com.ticticboooom.mods.mm.registration;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MM.ID);
    public static final IRecipeType<MachineStructureRecipe> MACHINE_STRUCTURE = IRecipeType.register("machine_structure");
    public static final IRecipeType<MachineProcessRecipe> MACHINE_PROCESS = IRecipeType.register("machine_process");

    public static final RegistryObject<MachineStructureRecipe.Serializer> STRUCTURE = RECIPE_SERIALIZERS.register("machine_structure", MachineStructureRecipe.Serializer::new);
    public static final RegistryObject<MachineProcessRecipe.Serializer> PROCESS = RECIPE_SERIALIZERS.register("machine_process", MachineProcessRecipe.Serializer::new);
}
