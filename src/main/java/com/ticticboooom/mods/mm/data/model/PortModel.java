package com.ticticboooom.mods.mm.data.model;

import com.ticticboooom.mods.mm.data.model.base.BlockstateModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PortModel {
    public ResourceLocation controllerId;
    public ResourceLocation type;
    public ResourceLocation id;
    public ITextComponent name;
    public BlockstateModel defaultModel;
    public boolean showCutout;
    public boolean input;
}
