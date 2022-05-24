package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

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

    @Override
    public void onBlueprintInitialRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        ModifiableStructureKeyType.Value data = (ModifiableStructureKeyType.Value) dataIn;
        data.initialRenderBlocks = new HashMap<>();
        data.currentRenderingModifier = 0;
        data.intervalTicker = 0;
        for (Map.Entry<String, StructureModel.Key> entry : data.modifiers.entrySet()) {
            StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(entry.getValue().type);
            value.onBlueprintInitialRender(pos, model, entry.getValue().data);
        }
    }

    @Override
    public GuiBlockRenderBuilder onBlueprintRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        ModifiableStructureKeyType.Value data = (ModifiableStructureKeyType.Value) dataIn;
        int counter = -1;
        for (Map.Entry<String, StructureModel.Key> entry : data.modifiers.entrySet()) {
            counter++;
            if (counter != data.currentRenderingModifier) {
                continue;
            }
            StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(entry.getValue().type);
            GuiBlockRenderBuilder guiBlockRenderBuilder = value.onBlueprintRender(pos, model, entry.getValue().data);
            if (!data.initialRenderBlocks.containsKey(entry.getKey())) {
                data.initialRenderBlocks.put(entry.getKey(), guiBlockRenderBuilder);
                return guiBlockRenderBuilder;
            } else {
                if (data.intervalTicker >= 1) {
                    data.intervalTicker = 0;
                    if (data.initialRenderBlocks.get(entry.getKey()) == guiBlockRenderBuilder) {
                        data.currentRenderingModifier++;
                        if (data.currentRenderingModifier >= data.modifiers.size()) {
                            data.initialRenderBlocks = new HashMap<>();
                            data.currentRenderingModifier = 0;
                            return onBlueprintRender(pos, model, dataIn);
                        }
                        continue;
                    } else {
                        return guiBlockRenderBuilder;
                    }
                } else {
                    data.intervalTicker += 0.01;
                    return guiBlockRenderBuilder;
                }
            }
        }
        return null;
    }

    public static final class Value implements StructureKeyTypeValue {
        public Map<String, StructureModel.Key> modifiers;
        public Map<String, GuiBlockRenderBuilder> initialRenderBlocks;
        public int currentRenderingModifier;
        public float intervalTicker;
    }
}
