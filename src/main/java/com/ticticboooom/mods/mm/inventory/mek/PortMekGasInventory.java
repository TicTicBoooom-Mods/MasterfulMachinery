package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasTank;

public class PortMekGasInventory implements IGasHandler, IGasTank {
    private GasStack stack = GasStack.EMPTY;
    private long capacity;

    public PortMekGasInventory(long capacity) {

        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public GasStack getChemicalInTank(int i) {
        return i == 0 ? stack : GasStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int i, GasStack stack) {
        if (i == 0) {
            this.stack = stack;
        }
    }

    @Override
    public long getTankCapacity(int i) {
        return capacity;
    }

    @Override
    public boolean isValid(int i, GasStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.isEmpty() || stack.getType() == this.stack.getType();
    }

    @Override
    public GasStack insertChemical(int i, GasStack stack, Action action) {
        if (!isValid(i, stack)) {
            return stack;
        }
        if (action.simulate()) {
            if (this.stack.getAmount() + stack.getAmount() > capacity) {
                return new GasStack(stack.getType(), this.stack.getAmount() + stack.getAmount() - capacity);
            } else {
                return GasStack.EMPTY;
            }
        }

        if (this.stack.getAmount() + stack.getAmount() > capacity) {
            long preAmount = this.stack.getAmount();
            if (this.stack.isEmpty()){
                this.stack = new GasStack(stack.getType(), capacity);
            } else {
                this.stack.setAmount(capacity);
            }
            return new GasStack(stack.getType(), preAmount  + stack.getAmount() - capacity);
        } else {
            if (this.stack.isEmpty()) {
                this.stack = new GasStack(stack.getType(), this.stack.getAmount() + stack.getAmount());
            } else {
                this.stack.setAmount(this.stack.getAmount() + stack.getAmount());
            }
            return GasStack.EMPTY;
        }
    }


    @Override
    public GasStack extractChemical(int i, long l, Action action) {
        if (!isValid(i, stack)) {
            return stack;
        }

        if (action.simulate()) {
            if (stack.getAmount() - l < 0) {
                return new GasStack(stack.getType(), Math.abs(stack.getAmount() - l));
            } else {
                return new GasStack(stack.getType(), l);
            }
        }

        if (stack.getAmount() - l < 0) {
            long preAmount = stack.getAmount();
            this.stack = GasStack.EMPTY;
            return new GasStack(stack.getType(), Math.abs(preAmount - l));
        } else {
            stack.setAmount(stack.getAmount() - l);
            return new GasStack(stack.getType(), l);
        }
    }

    @Override
    public GasStack getStack() {
        return stack;
    }

    @Override
    public void setStack(GasStack stack) {
        this.stack = stack;
    }

    @Override
    public void setStackUnchecked(GasStack stack) {
        this.stack = stack;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public boolean isValid(GasStack stack) {
        return isValid(0, stack);
    }

    @Override
    public void onContentsChanged() {

    }
}
