package com.ticticboooom.mods.mm.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.ProcessModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.process.PreProcessorType;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ProcessReloadListener extends JsonReloadListener {
    public static final Gson GSON = new Gson();

    public ProcessReloadListener() {
        super(GSON, "mm/processes");
    }


    @SubscribeEvent
    public static void on(AddReloadListenerEvent event) {
        event.addListener(new ProcessReloadListener());
    }


    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            DataRegistry.PROCESSES.put(entry.getKey(), parse(entry.getKey(), entry.getValue().getAsJsonObject()));
        }
    }

    private ProcessModel parse(ResourceLocation res, JsonObject json) {
        ProcessModel model = new ProcessModel();
        model.structure = ParserUtils.parseOrDefault(json, "structure", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        model.ticks = ParserUtils.parseOrDefault(json, "ticks", JsonElement::getAsInt, 0);
        model.inputs = ParserUtils.parseOrDefault(json, "inputs", x -> {
            JsonObject obj = x.getAsJsonObject();
            Map<String, ProcessIngredientType.Value> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                JsonObject inputObj = entry.getValue().getAsJsonObject();
                String type = inputObj.get("type").getAsString();
                ProcessIngredientType value = MMRegistries.PROCESS_INGREDIENT_TYPES.getValue(ResourceLocation.tryCreate(type));
                ProcessIngredientType.Value parse = value.parse(inputObj);
                result.put(entry.getKey(), parse);
            }
            return result;
        }, null);

        model.outputs = ParserUtils.parseOrDefault(json, "outputs", x -> {
            JsonObject obj = x.getAsJsonObject();
            Map<String, ProcessOutputType.Value> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                JsonObject inputObj = entry.getValue().getAsJsonObject();
                String type = inputObj.get("type").getAsString();
                ProcessOutputType value = MMRegistries.PROCESS_OUTPUT_TYPES.getValue(ResourceLocation.tryCreate(type));
                ProcessOutputType.Value parse = value.parse(inputObj);
                result.put(entry.getKey(), parse);
            }
            return result;
        }, null);

        model.preprocessors = ParserUtils.parseOrDefault(json, "preprocessors",  x -> {
            JsonObject obj = x.getAsJsonObject();
            Map<String, PreProcessorType.Value> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                JsonObject inputObj = entry.getValue().getAsJsonObject();
                String type = inputObj.get("type").getAsString();
                PreProcessorType value = MMRegistries.PREPROCESSOR_TYPES.getValue(ResourceLocation.tryCreate(type));
                PreProcessorType.Value parse = value.parse(inputObj);
                parse.type = ResourceLocation.tryCreate(type);
                result.put(entry.getKey(), parse);
            }
            return result;
        }, null);

        return model;
    }
}
