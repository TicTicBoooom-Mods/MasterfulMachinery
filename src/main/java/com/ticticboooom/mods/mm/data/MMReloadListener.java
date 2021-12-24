package com.ticticboooom.mods.mm.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MMReloadListener extends JsonReloadListener {
    public static final Gson GSON = new Gson();

    public MMReloadListener() {
        super(GSON, "masterfulmachinery/machines");
    }

    @SubscribeEvent
    public static void on(AddReloadListenerEvent event) {
        event.getListeners().add(new MMReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        
    }
}
