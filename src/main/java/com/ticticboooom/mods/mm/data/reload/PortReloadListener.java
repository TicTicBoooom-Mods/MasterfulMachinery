package com.ticticboooom.mods.mm.data.reload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.data.model.base.BlockstateModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PortReloadListener extends JsonReloadListener {
    public static final Gson GSON = new Gson();

    public PortReloadListener() {
        super(GSON, "masterfulmachinery/ports");
    }

    @SubscribeEvent
    public static void on(AddReloadListenerEvent event) {
        event.addListener(new PortReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            DataRegistry.PORTS.put(entry.getKey(), parse(entry.getKey(), entry.getValue().getAsJsonObject()));
        }
    }

    private PortModel parse(ResourceLocation res, JsonObject json) {
        PortModel model = new PortModel();
        model.id = res;
        model.type = ResourceLocation.tryCreate(json.get("type").getAsString());
        model.name = ParserUtils.parseTextComponent(json.get("name"));
        model.controllerId = ResourceLocation.tryCreate(json.get("controllerId").getAsString());
        model.defaultModel = ParserUtils.parseOrDefault(json, "defaultModel", (x) -> ParserUtils.parseBlockState(x.getAsJsonObject()), BlockstateModel.DEFAULT);
        model.showCutout = ParserUtils.parseOrDefault(json, "showCutout", JsonElement::getAsBoolean, true);
        return model;
    }
}
