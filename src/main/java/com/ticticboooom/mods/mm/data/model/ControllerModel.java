package com.ticticboooom.mods.mm.data.model;

import com.ticticboooom.mods.mm.data.model.base.BlockstateModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ControllerModel {
    public ResourceLocation id;
    public ITextComponent name;
    public BlockstateModel defaultModel;
    public boolean showCutout;

    public ControllerModel copy() {
        ControllerModel result = new ControllerModel();
        result.id = id;
        result.name = name;
        result.defaultModel = defaultModel.copy();
        result.showCutout = showCutout;
        return result;
    }
}
