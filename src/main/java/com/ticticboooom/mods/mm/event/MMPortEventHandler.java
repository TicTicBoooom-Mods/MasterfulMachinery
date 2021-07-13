package com.ticticboooom.mods.mm.event;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.registration.MMPorts;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MM.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MMPortEventHandler {
    @SubscribeEvent
    public static void onMMPorts(MMPortRegistrationEvent event) {
        MMPorts.init();
    }

}
