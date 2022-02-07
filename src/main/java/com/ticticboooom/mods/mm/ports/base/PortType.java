package com.ticticboooom.mods.mm.ports.base;

import com.google.gson.JsonObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public abstract class PortType extends ForgeRegistryEntry<PortType> {
    private final PortProcessor inputProcessorSupplier;
    private final PortProcessor outputProcessorSupplier;

    public PortType(PortProcessor inputProcessor, PortProcessor outputProcessor) {
        this.inputProcessorSupplier = inputProcessor;
        this.outputProcessorSupplier = outputProcessor;
    }

    public abstract PortStorage parseStorage(JsonObject data);
    public TileEntity createTileEntity() {
        return null;
    }
    public abstract ResourceLocation getInputCutout();
    public abstract ResourceLocation getOutputCutout();
}
