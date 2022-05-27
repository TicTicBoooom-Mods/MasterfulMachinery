package com.ticticboooom.mods.mm.ports.mekanism.pigment;

import mekanism.api.Action;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.PigmentStack;

public class MMPigmentHandler implements IPigmentHandler {

    private PigmentStack stack = PigmentStack.EMPTY;
    private long capacity;

    public MMPigmentHandler(long capacity){
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public PigmentStack getChemicalInTank(int i) {
        if (i != 0){
            return PigmentStack.EMPTY;
        }
        return stack;
    }

    @Override
    public void setChemicalInTank(int i, PigmentStack stack) {
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
    public boolean isValid(int i, PigmentStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.contains(stack);
    }

    @Override
    public PigmentStack insertChemical(int i, PigmentStack stack, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (!this.stack.isTypeEqual(stack)) return PigmentStack.EMPTY;
        if (this.stack.getAmount() + stack.getAmount() > capacity) return PigmentStack.EMPTY;
        if (stack.getAmount() > 8000) return PigmentStack.EMPTY;
        if (!simulate){
            PigmentStack result = new PigmentStack(this.stack.getType(),this.stack.getAmount() + stack.getAmount());
            this.stack = result;
            return result;
        }
        return PigmentStack.EMPTY;
    }


    @Override
    public PigmentStack extractChemical(int i, long l, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (l > stack.getAmount()) return PigmentStack.EMPTY;
        if (l > 8000) return PigmentStack.EMPTY;
        if (!simulate){
            PigmentStack result = new PigmentStack(this.stack.getType(),this.stack.getAmount() - l);
            this.stack = result;
            return result;
        }
        return PigmentStack.EMPTY;
    }

    public void setStack(PigmentStack stack) {
        this.stack = stack;
    }

}
