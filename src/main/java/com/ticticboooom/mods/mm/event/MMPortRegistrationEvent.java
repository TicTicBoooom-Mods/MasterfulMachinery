package com.ticticboooom.mods.mm.event;

import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

import java.util.Map;

public class MMPortRegistrationEvent extends Event implements IModBusEvent {
    @Getter
    private Map<ResourceLocation, MasterfulPortType> portsMap;

    public MMPortRegistrationEvent(Map<ResourceLocation, MasterfulPortType> portsMap) {

        this.portsMap = portsMap;
    }
}
