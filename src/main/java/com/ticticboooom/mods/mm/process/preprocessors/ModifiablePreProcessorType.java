package com.ticticboooom.mods.mm.process.preprocessors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.process.ModifierAction;
import com.ticticboooom.mods.mm.process.PreProcessorType;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifiablePreProcessorType extends PreProcessorType {
    @Override
    public Value parse(JsonObject json) {
        ModifiableValue result = new ModifiableValue();
        result.results = new HashMap<>();
        JsonObject modifierResults = json.get("modifier_results").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : modifierResults.entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();
            String type = obj.get("type").getAsString();
            ModifierAction value = MMRegistries.MODIFIER_ACTIONS.getValue(ResourceLocation.tryCreate(type));
            ModifierAction.Value parse = value.parse(obj);
            parse.type = ResourceLocation.tryCreate(type);
            result.results.put(entry.getKey(), parse);
        }
        return result;
    }

    @Override
    public boolean process(Value val, MachineProcessContext ctx) {
        ModifiableValue mval = (ModifiableValue) val;
        List<String> modifiers = ctx.structureCtx.activeModifiers;
        for (String entry : modifiers) {
            ModifierAction.Value maVal = ((ModifiableValue) val).results.get(entry);
            ModifierAction value = MMRegistries.MODIFIER_ACTIONS.getValue(maVal.type);
            if (!value.modify(maVal, ctx)){
                return false;
            }
        }
        return true;
    }

    public static final class ModifiableValue extends Value {
        public Map<String, ModifierAction.Value> results;
        public String name;
    }
}
