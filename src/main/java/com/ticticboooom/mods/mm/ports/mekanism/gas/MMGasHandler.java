package com.ticticboooom.mods.mm.ports.mekanism.gas;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import net.minecraftforge.fluids.FluidStack;

public class MMGasHandler implements IGasHandler {

    private GasStack stack = GasStack.EMPTY;
    private long capacity;

    public MMGasHandler(long capacity){
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public GasStack getChemicalInTank(int i) {
        if (i != 0){
            return GasStack.EMPTY;
        }
        return stack;
    }

    @Override
    public void setChemicalInTank(int i, GasStack stack) {
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
    public boolean isValid(int i, GasStack stack) {
        if (i != 0){
            return false;
        }
        return this.stack.contains(stack);
    }

    @Override
    public GasStack insertChemical(int i, GasStack stack, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (!this.stack.isTypeEqual(stack)) return GasStack.EMPTY;
        if (this.stack.getAmount() + stack.getAmount() > capacity) return GasStack.EMPTY;
        if (stack.getAmount() > 8000) return GasStack.EMPTY;
        if (!simulate){
            GasStack result = new GasStack(this.stack.getType(),this.stack.getAmount() + stack.getAmount());
            this.stack = result;
            return result;
        }
        return GasStack.EMPTY;
    }


    @Override
    public GasStack extractChemical(int i, long l, Action action) {
        boolean simulate = action == Action.SIMULATE;
        if (l > stack.getAmount()) return GasStack.EMPTY;
        if (l > 8000) return GasStack.EMPTY;
        if (!simulate){
            GasStack result = new GasStack(this.stack.getType(),this.stack.getAmount() - l);
            this.stack = result;
            return result;
        }
        return GasStack.EMPTY;
    }

    public void setStack(GasStack stack) {
        this.stack = stack;
    }

}
