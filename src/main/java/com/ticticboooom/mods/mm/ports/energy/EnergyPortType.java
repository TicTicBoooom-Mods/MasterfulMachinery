package com.ticticboooom.mods.mm.ports.energy;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import net.minecraft.util.ResourceLocation;

public class EnergyPortType extends PortType {
    @Override
    public PortStorage parseStorage(JsonObject data) {
        int stored = data.get("stored").getAsInt();
        int capacity = data.get("capacity").getAsInt();
        return new EnergyPortStorage(stored,capacity);
    }

    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/base_ports/energy_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/base_ports/energy_output_cutout");
    }
}
