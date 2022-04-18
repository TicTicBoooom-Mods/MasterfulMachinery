package com.ticticboooom.mods.mm.data;

import com.ticticboooom.mods.mm.process.PreProcessorType;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ProcessModel {
    public ResourceLocation structure;
    public int ticks;
    public Map<String, ProcessIngredientType.Value> inputs;
    public Map<String, ProcessOutputType.Value> outputs;
    public Map<String, PreProcessorType.Value> preprocessors;
}
