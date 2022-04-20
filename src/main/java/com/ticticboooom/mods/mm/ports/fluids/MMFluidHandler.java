package com.ticticboooom.mods.mm.ports.fluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class MMFluidHandler implements IFluidHandler {

    private FluidStack stack = FluidStack.EMPTY;
    private int amount;

    public MMFluidHandler(int amount) {
        this.amount = amount;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank != 0){
            return FluidStack.EMPTY;
        }
        return stack;
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank != 0){
            return 0;
        }
        return amount;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (tank != 0){
            return false;
        }
        return this.stack.containsFluid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!stack.isEmpty() && !resource.isFluidEqual(stack)) {
            return 0;
        }
        if (action == FluidAction.SIMULATE) {
            if ((long)resource.getAmount() + stack.getAmount() > amount) {
                return resource.getAmount() - (stack.getAmount()  + resource.getAmount() - amount);
            } else {
                return resource.getAmount();
            }
        }

        if ((long)resource.getAmount() + stack.getAmount() > amount) {
            int preAmount = stack.getAmount();
            if (stack.isEmpty()) {
                stack = new FluidStack(resource.getFluid(), amount);
            } else {
                stack.setAmount(amount);
            }
            return resource.getAmount() - (preAmount  + resource.getAmount() - amount);
        } else {
            if (stack.isEmpty()) {
                stack = new FluidStack(resource.getFluid(), resource.getAmount());
            } else {
                stack.setAmount(stack.getAmount() + resource.getAmount());
            }
            return resource.getAmount();
        }
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!stack.isEmpty() && !resource.isFluidEqual(stack)) {
            return FluidStack.EMPTY;
        }
        return innerDrain(resource.getAmount(), action == FluidAction.SIMULATE);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return innerDrain(maxDrain, action == FluidAction.SIMULATE);
    }

    private FluidStack innerDrain(int amount, boolean simulate) {
        if (simulate) {
            if (stack.getAmount() - amount <= 0) {
                return stack.copy();
            } else {
                return new FluidStack(stack.getFluid(), amount);
            }
        }

        if (stack.getAmount() - amount <= 0) {
            FluidStack preStack = stack.copy();
            stack.setAmount(0);
            return preStack;
        } else {
            stack.setAmount(stack.getAmount() - amount);
            return new FluidStack(stack.getFluid(), amount);
        }
    }

    public void setStack(FluidStack stack) {
        this.stack = stack;
    }
}
