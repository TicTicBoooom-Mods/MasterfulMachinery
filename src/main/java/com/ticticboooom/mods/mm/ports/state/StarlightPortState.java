package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.category.MMJeiPlugin;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarlightPortState extends PortState {
    public static final Codec<StarlightPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("amount").forGetter(z -> z.amount)
    ).apply(x, StarlightPortState::new));

    private int amount;

    public StarlightPortState(int starlight) {
        this.amount = starlight;
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {
        int current = amount;
        for (PortStorage inv : storage) {
            if (inv instanceof StarlightPortStorage) {
                StarlightPortStorage StarlightInv = (StarlightPortStorage) inv;
                current -= StarlightInv.getInv().extractStarlight(current, false);
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
            if (inv instanceof StarlightPortStorage) {
                StarlightPortStorage StarlightInv = (StarlightPortStorage) inv;
                current -= StarlightInv.getInv().extractStarlight(current, true);
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {
        /*
        int current = amount;
        for (PortStorage inv : storage) {
            if (inv instanceof StarlightPortStorage) {
                StarlightPortStorage StarlightInv = (StarlightPortStorage) inv;
                current -= StarlightInv.getInv().receiveStarlight(current, false);
                if (current <= 0) {
                    return;
                }
            }
        }
        */
    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        /*
        int current = amount;
        for (PortStorage inv : storage) {
            if (inv instanceof StarlightPortStorage) {
                StarlightPortStorage StarlightInv = (StarlightPortStorage) inv;
                current -= StarlightInv.getInv().receiveStarlight(current, true);
                if (current <= 0) {
                    return true;
                }
            }
        }
        */
        return true;
        // CHANGE TO FALSE
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "starlight");
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        // Am guessing is placeholder?
        return MMJeiPlugin.PRESSURE_TYPE;
    }

    @Override
    public <T> List<T> getIngredient(boolean input) {
        return new ArrayList<>();
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {

    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic slot = helpers.getGuiHelper().getSlotDrawable();
        slot.draw(ms, x, y);
    }
}
