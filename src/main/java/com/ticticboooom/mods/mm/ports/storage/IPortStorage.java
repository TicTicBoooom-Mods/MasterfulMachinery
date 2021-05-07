package com.ticticboooom.mods.mm.ports.storage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public interface IPortStorage {
    <T> LazyOptional<T> getLO();
    <T> boolean validate(Capability<T> cap);
}
