package com.ticticboooom.mods.mm.structures;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public abstract class StructureKeyType extends ForgeRegistryEntry<StructureKeyType> {
    public abstract boolean matches(JsonElement json);
    public abstract StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId);
}
