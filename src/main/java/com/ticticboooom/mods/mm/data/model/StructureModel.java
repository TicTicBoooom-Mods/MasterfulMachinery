package com.ticticboooom.mods.mm.data.model;

import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public class StructureModel {
    public ResourceLocation id;
    public ResourceLocation controllerId;
    public List<List<String>> pattern;
    public Map<Character, Key> keys;

    public static final class Key {
        public ResourceLocation type;
        public Object data;
    }
}
