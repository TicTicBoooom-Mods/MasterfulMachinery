package com.ticticboooom.mods.mm.ports;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.ports.energy.EnergyPortType;
import com.ticticboooom.mods.mm.ports.fluids.FluidPortType;
import com.ticticboooom.mods.mm.ports.items.ItemPortType;
import com.ticticboooom.mods.mm.ports.mekanism.gas.GasPortType;
import com.ticticboooom.mods.mm.ports.mekanism.infuse_type.InfuseTypePortType;
import com.ticticboooom.mods.mm.ports.mekanism.pigment.PigmentPortType;
import com.ticticboooom.mods.mm.ports.mekanism.slurry.SlurryPortType;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PortTypeRegistry {
    public static final Map<ResourceLocation, PortType> PORT_TYPES = new HashMap<>();

    public static void registerDefault() {
        PORT_TYPES.put(Ref.PortTypes.ITEM_TYPE, new ItemPortType());
        PORT_TYPES.put(Ref.PortTypes.FLUID_TYPE, new FluidPortType());
        PORT_TYPES.put(Ref.PortTypes.ENERGY_TYPE, new EnergyPortType());
        PORT_TYPES.put(Ref.PortTypes.MEKANISM_GAS, new GasPortType());
        PORT_TYPES.put(Ref.PortTypes.MEKANISM_SLURRY, new SlurryPortType());
        PORT_TYPES.put(Ref.PortTypes.MEKANISM_INFUSE_TYPE, new InfuseTypePortType());
        PORT_TYPES.put(Ref.PortTypes.MEKANISM_PIGMENT, new PigmentPortType());
    }
}
