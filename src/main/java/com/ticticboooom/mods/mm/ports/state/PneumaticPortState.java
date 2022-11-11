package com.ticticboooom.mods.mm.ports.state;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.MMJeiPlugin;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.ports.storage.PneumaticPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class PneumaticPortState extends PortState {

    public static final Codec<PneumaticPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.FLOAT.optionalFieldOf("minAir").forGetter(z -> Optional.of(z.minPressure)),
            Codec.FLOAT.fieldOf("air").forGetter(z -> z.pressure)
    ).apply(x, (min, pressure) -> new PneumaticPortState(min.orElse(pressure), pressure)));

    private float minPressure;
    private float pressure;

    public PneumaticPortState(float minPressure, float pressure) {
        this.minPressure = minPressure;
        this.pressure = pressure;
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof PneumaticPortStorage){
                PneumaticPortStorage pnc = (PneumaticPortStorage) portStorage;
                pnc.getInv().addAir(-(int) pressure);
            }
        }
    }

    @Override
    public boolean validateRequirement(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof PneumaticPortStorage){
                if (((PneumaticPortStorage) portStorage).getInv().getAir() >= this.minPressure){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof PneumaticPortStorage){
                PneumaticPortStorage pnc = (PneumaticPortStorage) portStorage;
                pnc.getInv().addAir((int)pressure);
            }
        }
    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof PneumaticPortStorage){
                return true;
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "pncr_pressure");
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return MMJeiPlugin.PRESSURE_TYPE;
    }

    @Override
    public <T> List<T> getIngredient(boolean input) {
        return (List<T>) Lists.newArrayList(new PressureStack(pressure));
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        IGuiIngredientGroup<PressureStack> group = layout.getIngredientsGroup(MMJeiPlugin.PRESSURE_TYPE);
        group.init(typeIndex, input, x+ 1, y +1);
        group.set(typeIndex, new PressureStack(pressure));
    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic slot = helpers.getGuiHelper().getSlotDrawable();
        slot.draw(ms, x, y);
    }
}
