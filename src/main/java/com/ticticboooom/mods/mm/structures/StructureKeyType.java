package com.ticticboooom.mods.mm.structures;

import com.google.gson.JsonElement;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public abstract class StructureKeyType extends ForgeRegistryEntry<StructureKeyType> {
    public abstract boolean matches(JsonElement json);
    public abstract StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId);
    public abstract boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world);
}
