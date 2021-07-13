package com.ticticboooom.mods.mm.event;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMPorts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = MM.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConstructEventHandler {
    @SubscribeEvent
    public static void onConstruct(FMLConstructModEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().post(new MMPortRegistrationEvent(MMPorts.PORTS));
        MMLoader.load();
    }
}
