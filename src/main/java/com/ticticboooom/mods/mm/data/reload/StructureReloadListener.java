package com.ticticboooom.mods.mm.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

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
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            DataRegistry.STRUCTURES.put(entry.getKey(), parse(entry.getKey(), entry.getValue().getAsJsonObject()));
        }
    }

    private StructureModel parse(ResourceLocation key, JsonObject asJsonObject) {

    }
}
