package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.SlurryStack;

public class PortMekSlurryInventory implements ISlurryHandler, ISlurryTank {
    private SlurryStack stack = SlurryStack.EMPTY;
    private long capacity;

    public PortMekSlurryInventory(long capacity) {

        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public SlurryStack getChemicalInTank(int i) {
        return i == 0 ? stack : SlurryStack.EMPTY;
    }

    @Override
    public void setChemicalInTank(int i, SlurryStack stack) {
        if (i == 0) {
            this.stack = stack;
        }
    }

    @Override
    public long getTankCapacity(int i) {
        return capacity;
    }

    @Override
    public boolean isValid(int i, SlurryStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.isEmpty() || stack.getType() == this.stack.getType();
    }

    @Override
    public SlurryStack insertChemical(int i, SlurryStack stack, Action action) {
        if (!isValid(i, stack)) {
            return stack;
        }
        if (action.simulate()) {
            if (this.stack.getAmount() + stack.getAmount() > capacity) {
                return new SlurryStack(stack.getType(), this.stack.getAmount() + stack.getAmount() - capacity);
            } else {
                return SlurryStack.EMPTY;
            }
        }

        if (this.stack.getAmount() + stack.getAmount() > capacity) {
            long preAmount = this.stack.getAmount();
            if (this.stack.isEmpty()){
                this.stack = new SlurryStack(stack.getType(), capacity);
            } else {
                this.stack.setAmount(capacity);
            }
            return new SlurryStack(stack.getType(), preAmount  + stack.getAmount() - capacity);
        } else {
            if (this.stack.isEmpty()) {
                this.stack = new SlurryStack(stack.getType(), this.stack.getAmount() + stack.getAmount());
            } else {
                this.stack.setAmount(this.stack.getAmount() + stack.getAmount());
            }
            return SlurryStack.EMPTY;
        }
    }


    @Override
    public SlurryStack extractChemical(int i, long l, Action action) {
        if (!isValid(i, stack)) {
            return stack;
        }

        if (action.simulate()) {
            if (stack.getAmount() - l < 0) {
                return new SlurryStack(stack.getType(), Math.abs(stack.getAmount() - l));
            } else {
                return new SlurryStack(stack.getType(), l);
            }
        }

        if (stack.getAmount() - l < 0) {
            long preAmount = stack.getAmount();
            this.stack = SlurryStack.EMPTY;
            return new SlurryStack(stack.getType(), Math.abs(preAmount - l));
        } else {
            stack.setAmount(stack.getAmount() - l);
            return new SlurryStack(stack.getType(), l);
        }
    }

    @Override
    public SlurryStack getStack() {
        return stack;
    }

    @Override
    public void setStack(SlurryStack stack) {
        this.stack = stack;
    }

    @Override
    public void setStackUnchecked(SlurryStack stack) {
        this.stack = stack;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public boolean isValid(SlurryStack stack) {
        return isValid(0, stack);
    }

    @Override
    public void onContentsChanged() {

    }
}
