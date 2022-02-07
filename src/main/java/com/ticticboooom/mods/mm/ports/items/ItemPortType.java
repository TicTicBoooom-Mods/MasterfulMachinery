package com.ticticboooom.mods.mm.ports.items;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import net.minecraft.util.ResourceLocation;

public class ItemPortType extends PortType {
    public ItemPortType() {
        super(new ItemPortInputProcessor(), new ItemPortOutputProcessor());
    }

    @Override
    public PortStorage parseStorage(JsonObject data) {
        int rows = data.get("rows").getAsInt();
        int columns = data.get("columns").getAsInt();
        return new ItemPortStorage(rows, columns);
    }

    @Override
    public ResourceLocation getInputCutout() {
        return Ref.res("block/base_ports/item_input_cutout");
    }

    @Override
    public ResourceLocation getOutputCutout() {
        return Ref.res("block/base_ports/item_output_cutout");
    }
}
