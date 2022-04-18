package com.ticticboooom.mods.mm.data;

import com.electronwill.nightconfig.core.conversion.ConversionTable;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DataRegistry {
    public static final Map<ResourceLocation, ControllerModel> CONTROLLERS = new HashMap<>();
    public static final Map<ResourceLocation, PortModel> PORTS = new HashMap<>();
    public static final Map<ResourceLocation, StructureModel> STRUCTURES = new HashMap<>();
    public static final Map<ResourceLocation, ProcessModel> PROCESSES = new HashMap<>();
}
