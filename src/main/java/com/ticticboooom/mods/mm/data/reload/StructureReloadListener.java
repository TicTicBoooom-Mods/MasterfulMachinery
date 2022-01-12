package com.ticticboooom.mods.mm.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
        super(GSON, "masterfulmachinery/machines");
    }

    @SubscribeEvent
    public static void on(AddReloadListenerEvent event) {
        event.addListener(new StructureReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        
    }
}
