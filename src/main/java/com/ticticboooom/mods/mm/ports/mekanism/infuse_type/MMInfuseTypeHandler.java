package com.ticticboooom.mods.mm.ports.mekanism.infuse_type;

import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfusionStack;

public class MMInfuseTypeHandler implements IInfusionHandler {

    private InfusionStack stack = InfusionStack.EMPTY;
    private long capacity;

    public MMInfuseTypeHandler(long capacity){
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public InfusionStack getChemicalInTank(int i) {
        if (i != 0){
            return InfusionStack.EMPTY;
        }
        return stack;
    }

    @Override
    public void setChemicalInTank(int i, InfusionStack stack) {
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
    public boolean isValid(int i, InfusionStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.contains(stack);
    }

    @Override
    public InfusionStack insertChemical(int i, InfusionStack stack, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (!this.stack.isTypeEqual(stack)) return InfusionStack.EMPTY;
        if (this.stack.getAmount() + stack.getAmount() > capacity) return InfusionStack.EMPTY;
        if (stack.getAmount() > 8000) return InfusionStack.EMPTY;
        if (!simulate){
            InfusionStack result = new InfusionStack(this.stack.getType(),this.stack.getAmount() + stack.getAmount());
            this.stack = result;
            return result;
        }
        return InfusionStack.EMPTY;
    }


    @Override
    public InfusionStack extractChemical(int i, long l, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (l > stack.getAmount()) return InfusionStack.EMPTY;
        if (l > 8000) return InfusionStack.EMPTY;
        if (!simulate){
            InfusionStack result = new InfusionStack(this.stack.getType(),this.stack.getAmount() - l);
            this.stack = result;
            return result;
        }
        return InfusionStack.EMPTY;
    }

    public void setStack(InfusionStack stack) {
        this.stack = stack;
    }

}
