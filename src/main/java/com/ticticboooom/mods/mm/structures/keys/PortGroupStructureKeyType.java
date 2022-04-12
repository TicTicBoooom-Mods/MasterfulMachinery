package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class PortGroupStructureKeyType extends StructureKeyType {

    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.PORT_GROUP.toString());
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        JsonObject obj = json.getAsJsonObject();
        Value result = new Value();
        result.group = obj.get("group").getAsString();
        return result;
    }

    public static final class Value implements StructureKeyTypeValue {
        public String group;
    }
}
