package com.ticticboooom.mods.mm.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import lombok.SneakyThrows;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.text.html.parser.Parser;
import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StructureReloadListener extends JsonReloadListener {
    public static final Gson GSON = new Gson();

    public StructureReloadListener() {
        super(GSON, "mm/machines");
    }

    @SubscribeEvent
    public static void on(AddReloadListenerEvent event) {
        event.addListener(new StructureReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        DataRegistry.STRUCTURES.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            DataRegistry.STRUCTURES.put(entry.getKey(), parse(entry.getKey(), entry.getValue().getAsJsonObject()));
        }
    }

    private StructureModel parse(ResourceLocation key, JsonObject json) {
        StructureModel result = new StructureModel();
        result.id = key;
        result.name = ParserUtils.parseTextComponent(json.get("name"));
        result.portGroupings = ParserUtils.parseOrDefault(json, "portGroupings", x -> {
            JsonObject obj = x.getAsJsonObject();
            Map<String, List<String>> res = new HashMap<>();
            Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
            for (Map.Entry<String, JsonElement> s : entries) {
                JsonArray arr = s.getValue().getAsJsonArray();
                ArrayList<String> strings = new ArrayList<>();
                for (JsonElement jsonElement : arr) {
                    strings.add(jsonElement.getAsString());
                }
                res.put(s.getKey(), strings);
            }
            return res;
        }, new HashMap<>());
        JsonObject requiredPortsJson = json.get("requiredPorts").getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> portKeys = requiredPortsJson.entrySet();
        result.requiredPorts = new HashMap<>();
        for (Map.Entry<String, JsonElement> portKey : portKeys) {
            JsonObject portObj = portKey.getValue().getAsJsonObject();
            StructureModel.RequiredPort requiredPort = new StructureModel.RequiredPort();
            requiredPort.port = ResourceLocation.tryCreate(portObj.get("port").getAsString());
            requiredPort.input = ParserUtils.parseOrDefault(portObj, "input", x -> Optional.of(x.getAsBoolean()), Optional.empty());
            requiredPort.tiers = ParserUtils.parseOrDefault(portObj, "tiers", x -> {
                JsonArray asJsonArray = x.getAsJsonArray();
                ArrayList<ResourceLocation> strings = new ArrayList<>();
                for (JsonElement jsonElement : asJsonArray) {
                    strings.add(ResourceLocation.tryCreate(jsonElement.getAsString()));
                }
                return strings;
            }, new ArrayList<>());
            result.requiredPorts.put(portKey.getKey(), requiredPort);
        }
        List<List<String>> pattern = new ArrayList<>();
        JsonArray patternJson = json.getAsJsonArray("pattern");
        for (JsonElement elem : patternJson) {
            JsonArray arr = elem.getAsJsonArray();
            ArrayList<String> strings = new ArrayList<>();
            for (JsonElement jsonElement : arr) {
                strings.add(new StringBuilder(jsonElement.getAsString()).reverse().toString());
            }
            pattern.add(strings);
        }
        result.pattern = pattern;
        JsonArray controllerId = json.getAsJsonArray("controllerId");
        result.controllerId = new ArrayList<>();
        for (JsonElement elem : controllerId) {
            result.controllerId.add(ResourceLocation.tryCreate(elem.getAsString()));
        }
        JsonObject keys = json.getAsJsonObject("keys");
        Set<Map.Entry<String, JsonElement>> entries = keys.entrySet();
        result.keys = new HashMap<>();
        for (Map.Entry<String, JsonElement> keyStr : entries) {
            JsonElement jsonElement = keyStr.getValue();
            StructureModel.Key parsedKey = parseKey(jsonElement, keyStr.getKey(), result.controllerId, key);
            result.keys.put(keyStr.getKey().charAt(0), parsedKey);
        }
        result = processStructure(result);
        return result;
    }

    private StructureModel.Key parseKey(JsonElement body, String key, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        for (StructureKeyType structureKeyType : MMRegistries.STRUCTURE_KEY_TYPES) {
            if (structureKeyType.matches(body)) {
                StructureKeyTypeValue parsed = structureKeyType.parse(body, controllerIds, structureId);
                StructureModel.Key result = new StructureModel.Key();
                result.data = parsed;
                result.type = structureKeyType.getRegistryName();
                return result;
            }
        }
        return null;
    }

    @SneakyThrows
    public BlockPos getControllerPos(StructureModel model) {
        for (int y = 0; y < model.pattern.size(); y++) {
            List<String> layer = model.pattern.get(y);
            for (int x = 0; x < layer.size(); x++) {
                String row = layer.get(x);
                for (int z = 0; z < row.length(); z++) {
                    if (row.charAt(z) == 'C') {
                        return new BlockPos(x, y, z);
                    }
                }
            }
        }
        throw new Exception("Structure: " + model.id + "never defines a controller position");
    }

    public StructureModel processStructure(StructureModel model) {
        List<StructureModel.PositionedKey> keys = new ArrayList<>();
        BlockPos controllerPos = getControllerPos(model);
        for (int y = 0; y < model.pattern.size(); y++) {
            List<String> layer = model.pattern.get(y);
            for (int x = 0; x < layer.size(); x++) {
                String row = layer.get(x);
                for (int z = 0; z < row.length(); z++) {
                    char c = row.charAt(z);
                    if (c == ' ') {
                        continue;
                    }
                    if (c == 'C') {
                        continue;
                    }
                    StructureModel.Key key = model.keys.get(c);
                    keys.add(new StructureModel.PositionedKey(new BlockPos(x, y, z).subtract(controllerPos), key));
                }
            }
        }
        model.positionedKeys = keys;
        return model;
    }
}
