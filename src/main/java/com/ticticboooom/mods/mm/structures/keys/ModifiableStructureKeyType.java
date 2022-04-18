package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import lombok.SneakyThrows;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
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
        HashMap<String, StructureModel.Key> modifiersMap = new HashMap<>();
        modifiers.entrySet().forEach(x -> {
            JsonElement jsonElement = x.getValue();
            for (StructureKeyType skt : MMRegistries.STRUCTURE_KEY_TYPES) {
                if (skt.matches(jsonElement)) {
                    modifiersMap.put(x.getKey(), new StructureModel.Key(skt.getRegistryName(), skt.parse(jsonElement, controllerIds, structureId)));
                    return;
                }
            }
            Ref.LOG.error("Error parsing modifiable structure key: could not find suitable sub type for modifier.");
        });

        Value value = new Value();
        value.modifiers = modifiersMap;
        return value;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        ModifiableStructureKeyType.Value data = (ModifiableStructureKeyType.Value) dataIn;
        for (Map.Entry<String, StructureModel.Key> entry : data.modifiers.entrySet()) {
            StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(entry.getValue().type);
            if (value.isValidPlacement(pos, model, state, entry.getValue().data, world, ctx)) {
                ctx.activeModifiers.add(entry.getKey());
                return true;
            }
        }
        return false;
    }

    public static final class Value implements StructureKeyTypeValue {
        public Map<String, StructureModel.Key> modifiers;
    }
}
