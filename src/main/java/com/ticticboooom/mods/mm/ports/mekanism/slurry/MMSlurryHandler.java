package com.ticticboooom.mods.mm.ports.mekanism.slurry;

import mekanism.api.Action;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.SlurryStack;

public class MMSlurryHandler implements ISlurryHandler {

    private SlurryStack stack = SlurryStack.EMPTY;
    private long capacity;

    public MMSlurryHandler(long capacity){
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public SlurryStack getChemicalInTank(int i) {
        if (i != 0){
            return SlurryStack.EMPTY;
        }
        return stack;
    }

    @Override
    public void setChemicalInTank(int i, SlurryStack stack) {
        setStack(stack);
    }

    @Override
    public long getTankCapacity(int i) {
        if (i != 0){
            return 0;
        }
        return capacity;
    }

    @Override
    public boolean isValid(int i, SlurryStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.contains(stack);
    }

    @Override
    public SlurryStack insertChemical(int i, SlurryStack stack, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (!this.stack.isTypeEqual(stack)) return SlurryStack.EMPTY;
        if (this.stack.getAmount() + stack.getAmount() > capacity) return SlurryStack.EMPTY;
        if (stack.getAmount() > 8000) return SlurryStack.EMPTY;
        if (!simulate){
            SlurryStack result = new SlurryStack(this.stack.getType(),this.stack.getAmount() + stack.getAmount());
            this.stack = result;
            return result;
        }
        return SlurryStack.EMPTY;
    }


    @Override
    public SlurryStack extractChemical(int i, long l, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (l > stack.getAmount()) return SlurryStack.EMPTY;
        if (l > 8000) return SlurryStack.EMPTY;
        if (!simulate){
            SlurryStack result = new SlurryStack(this.stack.getType(),this.stack.getAmount() - l);
            this.stack = result;
            return result;
        }
        return SlurryStack.EMPTY;
    }

    public void setStack(SlurryStack stack) {
        this.stack = stack;
    }

}
