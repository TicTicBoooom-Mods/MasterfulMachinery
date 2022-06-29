package com.ticticboooom.mods.mm.data.model;

import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StructureModel {
    public ResourceLocation id;
    public List<ResourceLocation> controllerId;
    public List<List<String>> pattern;
    public Map<Character, Key> keys;
    public Map<String, RequiredPort> requiredPorts;
    public Map<String, List<String>> portGroupings;
    public List<PositionedKey> positionedKeys;
    public ITextComponent name;

    public static class Key {
        public Key(ResourceLocation type, StructureKeyTypeValue data){
            this.type = type;
            this.data = data;
        }

        public Key() {

        }

        public ResourceLocation type;
        public StructureKeyTypeValue data;
    }

    public static final class PositionedKey extends Key {
        public PositionedKey(BlockPos pos, Key key) {
            this.pos = pos;
            this.data = key.data;
            this.type = key.type;
        }
        public BlockPos pos;
    }

    public static final class RequiredPort {
        public ResourceLocation port;
        public Optional<Boolean> input;
        public List<ResourceLocation> tiers;
    }
}
