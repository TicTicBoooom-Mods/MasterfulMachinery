package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MekChemicalInventory<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> implements IChemicalHandler<CHEMICAL, STACK>, IChemicalTank<CHEMICAL, STACK> {
    private STACK stack;
    private STACK empty;
    private BiFunction<CHEMICAL, Long, STACK> factory;
    private long capacity;

    public MekChemicalInventory(STACK empty, long capacity, BiFunction<CHEMICAL, Long, STACK> factory) {
        this.stack = (STACK) empty.copy();
        this.capacity = capacity;
        this.empty = empty;
        this.factory = factory;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public STACK getChemicalInTank(int i) {
        return stack;
    }

    @Override
    public void setChemicalInTank(int i, STACK stack) {
        if (i == 0){
            this.stack = stack;
        }
    }

    @Override
    public long getTankCapacity(int i) {
        return capacity;
    }

    @Override
    public boolean isValid(int i, STACK stack) {
        if (i != 0){
            return false;
        }
        return this.stack.isEmpty() || stack.getType() == this.stack.getType();
    }

    @Override
    public STACK insertChemical(int i, STACK stack, Action action) {
        if (!isValid(i, stack)) {
            return empty;
        }

        if (action.simulate()) {
            if (this.stack.getAmount() + stack.getAmount() > capacity) {
                return factory.apply(stack.getType(), (this.stack.getAmount() + stack.getAmount() - capacity));
            } else {
                return empty;
            }
        }

        if (this.stack.getAmount() + stack.getAmount() > capacity) {
            long preAmount = this.stack.getAmount();
            if (this.stack.isEmpty()){
                this.stack = factory.apply(stack.getType(), capacity);
            } else {
                this.stack.setAmount(capacity);
            }
            return factory.apply(stack.getType(), (preAmount  + stack.getAmount() - capacity));
        } else {
            if (this.stack.isEmpty()) {
                this.stack = factory.apply(stack.getType(), stack.getAmount());
            } else {
                this.stack.setAmount(this.stack.getAmount() + stack.getAmount());
            }
            return empty;
        }
    }

    @Override
    public STACK extractChemical(int i, long l, Action action) {
        if (action.simulate()) {
            if (stack.getAmount() - l < 0) {
                return factory.apply(stack.getType(), l - (l + stack.getAmount()));
            } else {
                return factory.apply(stack.getType(), l);
            }
        }

        if (stack.getAmount() - l < 0) {
            long preAmount = stack.getAmount();
            this.stack = empty;
            return factory.apply(stack.getType(),l - (preAmount - l));
        } else {
            stack.setAmount(stack.getAmount() - l);
            return factory.apply(stack.getType(), l);
        }
    }

    @Nonnull
    @Override
    public STACK getEmptyStack() {
        return empty;
    }

    @Override
    public STACK createStack(STACK stack, long l) {
        return factory.apply(stack.getType(), l);
    }

    @Override
    public STACK getStack() {
        return stack;
    }

    @Override
    public void setStack(STACK stack) {
        this.stack = stack;
    }

    @Override
    public void setStackUnchecked(STACK stack) {
        this.stack = stack;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public boolean isValid(STACK stack) {
        return isValid(0, stack);
    }

    @Override
    public void onContentsChanged() {

    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
