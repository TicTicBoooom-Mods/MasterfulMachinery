package com.ticticboooom.mods.mm.integration.jei;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.integration.jei.ingredients.controller.ControllerIngredientHelper;
import com.ticticboooom.mods.mm.integration.jei.ingredients.controller.ControllerIngredientRenderer;
import com.ticticboooom.mods.mm.integration.jei.ingredients.controller.ControllerIngredientType;
import com.ticticboooom.mods.mm.integration.jei.ingredients.controller.ControllerIngredientTypeInterpreter;
import com.ticticboooom.mods.mm.setup.MMItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Map;

@JeiPlugin
public class MMJeiPlugin implements IModPlugin {

    public static final ControllerIngredientType CONTROLLER_TYPE = new ControllerIngredientType();
    public static final ControllerIngredientHelper CONTROLLER_HELPER = new ControllerIngredientHelper();
    public static final ControllerIngredientRenderer CONTROLLER_RENDERER = new ControllerIngredientRenderer();

    @Override
    public ResourceLocation getPluginUid() {
        return Ref.JEI_PLUGIN;
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        ArrayList<ItemStack> controllers = new ArrayList<>();
        for (Map.Entry<ResourceLocation, ControllerModel> entry : DataRegistry.CONTROLLERS.entrySet()) {
            ItemStack stack = new ItemStack(MMItems.CONTROLLER.get());
            CompoundNBT tag = stack.getOrCreateTag();
            tag.putString("Controller", entry.getValue().id.toString());
            controllers.add(stack);
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(MMItems.CONTROLLER.get(), new ControllerIngredientTypeInterpreter());
    }
}
