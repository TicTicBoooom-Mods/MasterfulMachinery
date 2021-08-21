package com.ticticboooom.mods.mm.registration;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.ports.parser.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class MMPorts {
    public static Map<ResourceLocation, MasterfulPortType> PORTS = new HashMap<>();

    public static void init() {
        PORTS.put(new ResourceLocation(MM.ID, "items"), new MasterfulPortType(new ResourceLocation(MM.ID, "items"), new ItemPortParser()));
        PORTS.put(new ResourceLocation(MM.ID, "fluids"), new MasterfulPortType(new ResourceLocation(MM.ID, "fluids"), new FluidPortParser()));
        PORTS.put(new ResourceLocation(MM.ID, "energy"),new MasterfulPortType(new ResourceLocation(MM.ID, "energy"), new EnergyPortParser()));
//        PORTS.put(new ResourceLocation(MM.ID, "weather"),new MasterfulPortType(new ResourceLocation(MM.ID, "weather"), new WeatherPortParser()));
        if (ModList.get().isLoaded("mekanism")) {
            PORTS.put(new ResourceLocation(MM.ID, "mekanism_gas"),new MasterfulPortType(new ResourceLocation(MM.ID, "mekanism_gas"), new MekGasPortParser()));
            PORTS.put(new ResourceLocation(MM.ID, "mekanism_slurry"),new MasterfulPortType(new ResourceLocation(MM.ID, "mekanism_slurry"), new MekSlurryPortParser()));
            PORTS.put(new ResourceLocation(MM.ID, "mekanism_infuse"),new MasterfulPortType(new ResourceLocation(MM.ID, "mekanism_infuse"), new MekInfusePortParser()));
        }
        if (ModList.get().isLoaded("pneumaticcraft")){
            PORTS.put(new ResourceLocation(MM.ID, "pncr_pressure"),new MasterfulPortType(new ResourceLocation(MM.ID, "pncr_pressure"), new PneumaticPortParser()));
        }
        if (ModList.get().isLoaded("create")){
            PORTS.put(new ResourceLocation(MM.ID, "create_rotation"),new MasterfulPortType(new ResourceLocation(MM.ID, "create_rotation"), new RotationPortParser()));
        }
        if (ModList.get().isLoaded("botania")){
            PORTS.put(new ResourceLocation(MM.ID, "botania_mana"),new MasterfulPortType(new ResourceLocation(MM.ID, "botania_mana"), new ManaPortParser()));
        }
        if (ModList.get().isLoaded("astralsorcery")){
            PORTS.put(new ResourceLocation(MM.ID, "astral_starlight"),new MasterfulPortType(new ResourceLocation(MM.ID, "astral_starlight"), new StarlightPortParser()));
        }
    }
}
