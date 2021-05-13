package com.ticticboooom.mods.mm.ports.state;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.exception.InvalidProcessDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.FluidPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import lombok.SneakyThrows;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.fluid.Fluid;
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
    public void processRequirement(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
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
    public boolean validateRequirement(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
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
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
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
    public boolean validateResult(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
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

    @SneakyThrows
    @Override
    public void validateDefinition() {
        if (!RLUtils.isRL(fluid)) {
            throw new InvalidProcessDefinitionException("Fluid: " + fluid + " is not a valid id (ResourceLocation)");
        }
        if (!ForgeRegistries.FLUIDS.containsKey(RLUtils.toRL(fluid))) {
            throw new InvalidProcessDefinitionException("Fluid: " + fluid + " does not exist in the game's registered fluids");
        }
    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic slot = helpers.getGuiHelper().getSlotDrawable();
        slot.draw(ms, x, y);
    }


    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        layout.getFluidStacks().init(typeIndex, input, x + 1, y + 1, 16, 16, 1, false, null);
        layout.getFluidStacks().set(typeIndex, new FluidStack(ForgeRegistries.FLUIDS.getValue(RLUtils.toRL(fluid)), 1));
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return VanillaTypes.FLUID;
    }

    @Override
    public <T> List<T> getIngredient(boolean input) {
        return (List<T>) ImmutableList.of(new FluidStack(ForgeRegistries.FLUIDS.getValue(RLUtils.toRL(fluid)), amount));
    }
}
