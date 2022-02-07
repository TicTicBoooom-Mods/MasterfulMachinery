package com.ticticboooom.mods.mm.ports.items;

import com.ticticboooom.mods.mm.ports.base.PortStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class ItemPortStorage extends PortStorage {


    private final int rows;
    private final int columns;

    public ItemPortStorage(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }


    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        return null;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return null;
    }

    @Override
    public void load(CompoundNBT nbt) {

    }
}
