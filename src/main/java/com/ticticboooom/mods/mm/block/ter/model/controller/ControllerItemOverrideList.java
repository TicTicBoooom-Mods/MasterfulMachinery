package com.ticticboooom.mods.mm.block.ter.model.controller;

import com.ticticboooom.mods.mm.block.ter.model.DefaultBakedModel;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.base.BlockstateModel;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import net.java.games.input.Controller;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.ModelDataMap;

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
        return DataRegistry.CONTROLLERS.get(resourceLocation).defaultModel.getModel();
    }
}