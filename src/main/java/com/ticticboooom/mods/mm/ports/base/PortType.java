package com.ticticboooom.mods.mm.ports.base;

import com.google.gson.JsonObject;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public abstract class PortType extends ForgeRegistryEntry<PortType> {
    private final Supplier<PortProcessor> inputProcessorSupplier;
    private final Supplier<PortProcessor> outputProcessorSupplier;

    public PortType(Supplier<PortProcessor> inputProcessor, Supplier<PortProcessor> outputProcessor) {
        this.inputProcessorSupplier = inputProcessor;
        this.outputProcessorSupplier = outputProcessor;
    }

    public abstract PortStorage parseStorage(JsonObject data);
}
