package com.ticticboooom.mods.mm.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlueprintDataProvider implements ICapabilitySerializable<INBT> {

    private final BlueprintData capability = new BlueprintData();
    private final LazyOptional<IBlueprintData> capabilityLO = LazyOptional.of(() -> capability);

    @Override
    public INBT serializeNBT() {
        return Capabilities.BLUEPRINT_DATA.writeNBT(capability, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        Capabilities.BLUEPRINT_DATA.readNBT(capability, null, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.BLUEPRINT_DATA) {
            return capabilityLO.cast();
        }
        return LazyOptional.empty();
    }
}
