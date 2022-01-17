package com.ticticboooom.mods.mm.data.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.types.Func;
import com.ticticboooom.mods.mm.data.model.base.BlockstateModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ParserUtils {
    public static ITextComponent parseTextComponent(JsonElement elem) {
        if (elem.isJsonObject()) {
            JsonObject nameObj = elem.getAsJsonObject();
            String type = nameObj.get("type").getAsString();
            String key = nameObj.get("key").getAsString();
            if (type.equals("translation")) {
                return new TranslationTextComponent(key);
            } else if (type.equals("string")) {
                return new StringTextComponent(key);
            }
        }
        return new StringTextComponent(elem.getAsString());
    }

    public static BlockstateModel parseBlockState(JsonObject obj) {
        BlockstateModel model = new BlockstateModel();
        model.block = ResourceLocation.tryCreate(obj.get("block").getAsString());
        model.properties = new HashMap<>();
        JsonObject properties = obj.get("properties").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : properties.entrySet()) {
            model.properties.put(entry.getKey(), entry.getValue().getAsString());
        }
        return model;
    }

    public static <R> R parseOrDefault(JsonObject json, String key, Function<JsonElement, R> parse, R defaultValue) {
        if (json.has(key)) {
            return parse.apply(json.get(key));
        } else {
            return defaultValue;
        }
    }
}
