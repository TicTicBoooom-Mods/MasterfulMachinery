package com.ticticboooom.mods.mm.ports;

import com.ticticboooom.mods.mm.ports.parser.PortFactory;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class MasterfulPortType implements IForgeRegistryEntry<MasterfulPortType> {

    private ResourceLocation name;
    @Getter
    private PortFactory parser;

    public MasterfulPortType(ResourceLocation name, PortFactory parser) {
        this.name = name;
        this.parser = parser;
    }

    @Override
    public MasterfulPortType setRegistryName(ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return name;
    }

    @Override
    public Class<MasterfulPortType> getRegistryType() {
        return (Class<MasterfulPortType>) getClass();
    }


}
