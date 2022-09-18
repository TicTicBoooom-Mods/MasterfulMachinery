package com.ticticboooom.mods.mm.ports.base;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class PortType extends ForgeRegistryEntry<PortType> {
    public abstract PortStorage parseStorage(JsonObject data);
    public TileEntity createTileEntity() {
        return MMTiles.PORT.get().create();
    }
    public abstract ResourceLocation getInputCutout();
    public abstract ResourceLocation getOutputCutout();
}
