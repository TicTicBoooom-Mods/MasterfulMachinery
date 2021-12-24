package com.ticticboooom.mods.mm.ports.base;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class PortStorage {
    public abstract <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction);
    public abstract CompoundNBT save(CompoundNBT nbt);
    public abstract void load(CompoundNBT nbt);
}
