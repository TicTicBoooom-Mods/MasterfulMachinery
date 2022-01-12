package com.ticticboooom.mods.mm.data.model.base;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

public class BlockstateModel {
    public ResourceLocation block;
    public Map<String, String> properties;

    public BlockState createState() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Name", block.toString());
        CompoundNBT props = new CompoundNBT();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            props.putString(entry.getKey(), entry.getValue());
        }
        nbt.put("Properties", props);
        return NBTUtil.readBlockState(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    public IBakedModel getModel() {
        return Minecraft.getInstance().getModelManager().getModel(BlockModelShapes.getModelLocation(createState()));
    }

    public BlockstateModel copy() {
        BlockstateModel result = new BlockstateModel();
        result.block = block;
        result.properties = new HashMap<>();
        result.properties.putAll(properties);
        return result;
    }
}
