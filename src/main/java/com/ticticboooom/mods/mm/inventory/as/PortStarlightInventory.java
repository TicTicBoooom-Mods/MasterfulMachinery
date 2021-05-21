package com.ticticboooom.mods.mm.inventory.as;

public class PortStarlightInventory implements IStarlightStorage {

    private int stored;
    private int capacity;
    public PortStarlightInventory(int stored, int capacity) {

        this.stored = stored;
        this.capacity = capacity;
    }

    @Override
    public int receiveStarlight(int maxReceive, boolean simulate) {
        if (simulate) {
            if ((long)maxReceive + stored > capacity) {
                return maxReceive - (stored + maxReceive - capacity);
            } else {
                return maxReceive;
            }
        }
        if ((long)maxReceive + stored > capacity) {
            int result = maxReceive - (stored + maxReceive - capacity);
            stored = capacity;
            return result;
        } else {
            stored += maxReceive;
            return maxReceive;
        }
    }

    @Override
    public int extractStarlight(int maxExtract, boolean simulate) {
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
    public int getStarlightStored() {
        return stored;
    }

    @Override
    public int getMaxStarlightStored() {
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
