package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class PortTierStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.PORT_TIER.toString());
    }

    @Override
    public Object parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
    }

    public static final class Value {
        public ResourceLocation portTier;
        public Optional<Boolean> input;
    }
}
