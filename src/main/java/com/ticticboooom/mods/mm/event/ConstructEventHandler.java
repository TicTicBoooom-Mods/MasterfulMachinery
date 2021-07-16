package com.ticticboooom.mods.mm.event;

import com.ticticboooom.mods.mm.registration.MMLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConstructEventHandler {
    @SubscribeEvent
    public static void onConstruct(FMLConstructModEvent event) {
        MMLoader.load();
    }
}
