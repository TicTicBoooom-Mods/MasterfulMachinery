package com.ticticboooom.mods.mm.block.ter.model;

import com.google.common.collect.ImmutableList;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverride;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ControllerItemOverrideList extends ItemOverrideList {
    @Nullable
    @Override
    public IBakedModel getOverrideModel(IBakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
        if (!stack.hasTag()) {
            return super.getOverrideModel(model, stack, world, livingEntity);
        }
        CompoundNBT tag = stack.getTag();
        if (!tag.contains("Controller")){
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        String controller = tag.getString("Controller");
        ResourceLocation resourceLocation = ResourceLocation.tryCreate(controller);
        if (resourceLocation == null){
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        if (!DataRegistry.CONTROLLERS.containsKey(resourceLocation)) {
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        ControllerModel controllerModel = DataRegistry.CONTROLLERS.get(resourceLocation);
        return controllerModel.defaultModel.getModel();
    }
}
