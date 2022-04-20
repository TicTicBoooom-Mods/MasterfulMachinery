package com.ticticboooom.mods.mm.ports.fluids;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import net.minecraft.util.ResourceLocation;

public class FluidPortType extends PortType {
    @Override
    public PortStorage parseStorage(JsonObject data) {
        return new FluidPortStorage(data.get("capacity").getAsInt());
    }

    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/base_ports/fluid_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/base_ports/fluid_output_cutout");
    }
}
