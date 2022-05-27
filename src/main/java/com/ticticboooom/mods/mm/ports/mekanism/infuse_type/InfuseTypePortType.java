package com.ticticboooom.mods.mm.ports.mekanism.infuse_type;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import net.minecraft.util.ResourceLocation;

public class InfuseTypePortType extends PortType {
    @Override
    public PortStorage parseStorage(JsonObject data) {
        return new InfuseTypePortStorage(data.get("capacity").getAsLong());
    }


    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/compact_ports/mekanism_infusion_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/compact_ports/mekanism_infusion_output_cutout");
    }
}
