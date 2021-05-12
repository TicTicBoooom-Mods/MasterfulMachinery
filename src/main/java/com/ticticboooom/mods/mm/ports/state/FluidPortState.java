package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.FluidPortStorage;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class FluidPortState extends PortState {

    public static final Codec<FluidPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("amount").forGetter(z -> z.amount),
            Codec.STRING.fieldOf("fluid").forGetter(z -> z.fluid)
    ).apply(x, FluidPortState::new));


    @Getter
    private final int amount;
    @Getter
    private final String fluid;

    public FluidPortState(int amount, String fluid) {
        this.amount = amount;
        this.fluid = fluid;
    }

    @Override
    public void processRequirement(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof FluidPortStorage) {
                FluidPortStorage fInv = (FluidPortStorage) inv;
                FluidStack fluidInTank = fInv.getInv().getFluidInTank(0);
                if (!fluidInTank.getFluid().getRegistryName().toString().equals(fluid)) {
                    continue;
                }

                current -= fInv.getInv().drain(new FluidStack(fluidInTank.getFluid(), current), IFluidHandler.FluidAction.EXECUTE).getAmount();
                if (current <= 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateRequirement(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof FluidPortStorage) {
                FluidPortStorage fInv = (FluidPortStorage) inv;
                FluidStack fluidInTank = fInv.getInv().getFluidInTank(0);
                if (!fluidInTank.getFluid().getRegistryName().toString().equals(fluid)) {
                    continue;
                }

                current -= fInv.getInv().drain(new FluidStack(fluidInTank.getFluid(), current), IFluidHandler.FluidAction.SIMULATE).getAmount();
                if (current <= 0) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void processResult(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof FluidPortStorage) {
                FluidPortStorage fInv = (FluidPortStorage) inv;
                FluidStack fluidInTank = fInv.getInv().getFluidInTank(0);
                if (!fluidInTank.isEmpty() && !fluidInTank.getFluid().getRegistryName().toString().equals(fluid)) {
                    continue;
                }

                current -= fInv.getInv().fill(new FluidStack(ForgeRegistries.FLUIDS.getValue(RLUtils.toRL(fluid)), current), IFluidHandler.FluidAction.EXECUTE);
                if (current <= 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateResult(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof FluidPortStorage) {
                FluidPortStorage fInv = (FluidPortStorage) inv;
                FluidStack fluidInTank = fInv.getInv().getFluidInTank(0);
                if (!fluidInTank.isEmpty() && !fluidInTank.getFluid().getRegistryName().toString().equals(fluid)) {
                    continue;
                }

                current -= fInv.getInv().fill(new FluidStack(ForgeRegistries.FLUIDS.getValue(RLUtils.toRL(fluid)), current), IFluidHandler.FluidAction.SIMULATE);
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "fluids");
    }
}
