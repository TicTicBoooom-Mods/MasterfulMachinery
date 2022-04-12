package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import lombok.SneakyThrows;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifiableStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.MODIFIABLE.toString());
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        JsonObject obj = json.getAsJsonObject();
        JsonObject modifiers = obj.getAsJsonObject("modifiers");
        HashMap<String, StructureKeyTypeValue> modifiersMap = new HashMap<>();
        modifiers.entrySet().forEach(x -> {
            JsonElement jsonElement = x.getValue();
            for (StructureKeyType skt : MMRegistries.STRUCTURE_KEY_TYPES) {
                if (skt.matches(jsonElement)) {
                    modifiersMap.put(x.getKey(), skt.parse(jsonElement, controllerIds, structureId));
                    return;
                }
            }
            Ref.LOG.error("Error parsing modifiable structure key: could not find suitable sub type for modifier.");
        });

        Value value = new Value();
        value.modifiers = modifiersMap;
        return value;
    }

    public static final class Value implements StructureKeyTypeValue {
        public Map<String, StructureKeyTypeValue> modifiers;
    }
}
