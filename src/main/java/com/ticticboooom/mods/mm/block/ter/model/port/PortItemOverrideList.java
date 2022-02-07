package com.ticticboooom.mods.mm.block.ter.model.port;

import com.google.common.collect.Lists;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.ports.PortTypeRegistry;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.util.ModelTools;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PortItemOverrideList extends ItemOverrideList {
    @Nullable
    @Override
    public IBakedModel getOverrideModel(IBakedModel model, ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity livingEntity) {
        if (!stack.hasTag()) {
            return super.getOverrideModel(model, stack, world, livingEntity);
        }
        CompoundNBT tag = stack.getTag();
        if (!tag.contains("Port")){
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        String controller = tag.getString("Port");
        ResourceLocation resourceLocation = ResourceLocation.tryCreate(controller);
        if (resourceLocation == null){
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        if (!DataRegistry.PORTS.containsKey(resourceLocation)) {
            return super.getOverrideModel(model, stack, world, livingEntity);
        }

        PortModel portModel = DataRegistry.PORTS.get(resourceLocation);
        return portModel.defaultModel.getModel();
    }
}
