package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class BlockStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        if (json.isJsonPrimitive()){
            return true;
        }
        if (json.isJsonObject()){
            if (json.getAsJsonObject().has("include")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        Value result = new Value();
        result.blockSelector = new ArrayList<>();
        if (json.isJsonPrimitive()) {
            result.blockSelector.add(json.getAsString());
            return result;
        }
        if (json.isJsonObject()) {
            JsonObject jsonObj = json.getAsJsonObject();
            JsonElement includesJson = jsonObj.get("include");
            if (includesJson.isJsonPrimitive()) {
                result.blockSelector.add(includesJson.getAsString());
                return result;
            }

            JsonArray includes = includesJson.getAsJsonArray();
            includes.forEach(x -> result.blockSelector.add(x.getAsString()));
            result.properties = ParserUtils.parseOrDefault(jsonObj, "properties", x -> {
                HashMap<String, String> res = new HashMap<>();
                JsonObject obj = x.getAsJsonObject();
                obj.entrySet().forEach(z -> {
                    res.put(z.getKey(), z.getValue().getAsString());
                });
                return res;
            }, new HashMap<>());
        }
        return result;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        BlockStructureKeyType.Value data = (BlockStructureKeyType.Value) dataIn;
        boolean matches = false;
        for (String s : data.blockSelector) {
            if (s.startsWith("#")) {
                String tagName = s.substring(1);
                if (state.isIn(BlockTags.getCollection().getTagByID(Objects.requireNonNull(ResourceLocation.tryCreate(tagName))))) {
                    matches = true;
                }
            } else {
                if (state.getBlock().getRegistryName().toString().equals(s)) {
                    matches = true;
                }
            }
        }
        return matches;
    }

    public static final class Value  implements StructureKeyTypeValue {
        public List<String> blockSelector;
        public Map<String, String> properties;
    }
}
