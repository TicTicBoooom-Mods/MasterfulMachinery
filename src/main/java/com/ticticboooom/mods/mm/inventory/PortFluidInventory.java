package com.ticticboooom.mods.mm.inventory;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class PortFluidInventory implements IFluidHandler {

    private FluidStack stack = FluidStack.EMPTY;
    private final int capacity;
    public PortFluidInventory(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return tank != 0 ? FluidStack.EMPTY : stack.copy();
    }

    @Override
    public int getTankCapacity(int tank) {
        return tank != 0 ? 0 : capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return tank == 0 && (this.stack.isEmpty() || stack.isFluidEqual(this.stack));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!stack.isEmpty() && !resource.isFluidEqual(stack)) {
            return 0;
        }
        if (action == FluidAction.SIMULATE) {
            if (resource.getAmount() + stack.getAmount() > capacity) {
                return capacity - resource.getAmount();
            } else {
                return resource.getAmount();
            }
        }

        if (resource.getAmount() + stack.getAmount() > capacity) {
            if (stack.isEmpty()) {
                stack = new FluidStack(resource.getFluid(), capacity);
            } else {
                stack.setAmount(capacity);
            }
            return capacity - resource.getAmount();
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
            if (stack.getAmount() - amount < 0) {
                return stack.copy();
            } else {
                return new FluidStack(stack.getFluid(), amount);
            }
        }

        if (stack.getAmount() - amount < 0) {
            stack.setAmount(0);
            return stack.copy();
        } else {
            stack.setAmount(stack.getAmount() - amount);
            return new FluidStack(stack.getFluid(), amount);
        }
    }

    public void setStack(FluidStack stack) {
        this.stack = stack;
    }
}
