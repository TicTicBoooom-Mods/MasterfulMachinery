package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Object parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
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
                obj.keySet().forEach(z -> {
                    res.put(z, obj.get(z).getAsString());
                });
                return res;
            }, new HashMap<>());
        }
        return result;
    }

    public static final class Value {
        public List<String> blockSelector;
        public Map<String, String> properties;
    }
}
