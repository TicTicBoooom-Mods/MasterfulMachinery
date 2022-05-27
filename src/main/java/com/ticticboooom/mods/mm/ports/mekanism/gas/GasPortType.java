package com.ticticboooom.mods.mm.ports.mekanism.gas;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.ports.fluids.FluidPortStorage;
import net.minecraft.util.ResourceLocation;

public class GasPortType extends PortType {
    @Override
    public PortStorage parseStorage(JsonObject data) {
        return new GasPortStorage(data.get("capacity").getAsLong());
    }


    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/compact_ports/mekanism_gas_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/compact_ports/mekanism_gas_output_cutout");
    }
}
