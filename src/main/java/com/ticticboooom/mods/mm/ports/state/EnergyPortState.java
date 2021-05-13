package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.ports.storage.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class EnergyPortState extends PortState {

public static final Codec<EnergyPortState> CODEC  =RecordCodecBuilder.create(x -> x.group(
                Codec.INT.fieldOf("amount").forGetter(z -> z.amount)
            ).apply(x, EnergyPortState::new));


    @Getter
    private final int amount;

    public EnergyPortState(int amount) {
        this.amount = amount;
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().extractEnergy(current, false);
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
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().extractEnergy(current, true);
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
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().receiveEnergy(current, false);
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
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().receiveEnergy(current, true);
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "energy");
    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 18, 61, 4, 18);
        drawable.draw(ms, x, y);
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return null;
    }
}
