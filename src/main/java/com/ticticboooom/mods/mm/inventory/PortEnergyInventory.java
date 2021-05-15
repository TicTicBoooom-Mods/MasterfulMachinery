package com.ticticboooom.mods.mm.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.IEnergyStorage;
import tesseract.api.gt.GTConsumer;
import tesseract.api.gt.IEnergyHandler;
import tesseract.util.Dir;

public class PortEnergyInventory implements IEnergyStorage {

    private int stored;
    private int capacity;
    public PortEnergyInventory(int stored, int capacity) {

        this.stored = stored;
        this.capacity = capacity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (simulate) {
            if (maxReceive + stored > capacity) {
                return maxReceive > capacity ? capacity - stored : maxReceive;
            } else {
                return maxReceive;
            }
        }
        if (maxReceive + stored > capacity) {
            int result = maxReceive > capacity ? capacity - stored : maxReceive;
            stored = capacity;
            return result;
        } else {
            stored += maxReceive;
            return maxReceive;
        }

    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (simulate) {
            if (stored - maxExtract < 0) {
                return stored;
            } else {
                return maxExtract;
            }
        }
        if (stored - maxExtract < 0) {
            int result = stored;
            stored = 0;
            return result;
        } else {
            stored -= maxExtract;
            return maxExtract;
        }
    }

    @Override
    public int getEnergyStored() {
        return stored;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return stored > 0;
    }

    @Override
    public boolean canReceive() {
        return stored < capacity;
    }

    public void setStored(int amount) {
        this.stored = amount;
    }
}
