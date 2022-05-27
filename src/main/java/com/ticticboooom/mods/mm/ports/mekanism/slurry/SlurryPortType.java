package com.ticticboooom.mods.mm.ports.mekanism.slurry;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import net.minecraft.util.ResourceLocation;

public class SlurryPortType extends PortType {
    @Override
    public PortStorage parseStorage(JsonObject data) {
        return new SlurryPortStorage(data.get("capacity").getAsLong());
    }


    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/compact_ports/mekanism_slurry_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/compact_ports/mekanism_slurry_output_cutout");
    }
}
