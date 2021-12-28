package com.ticticboooom.mods.mm.ports.state;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.exception.InvalidProcessDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.MekPigmentPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.SneakyThrows;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.client.jei.ChemicalStackRenderer;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;
import java.util.Objects;

public class MekPigmentPortState extends PortState {

    public static final Codec<MekPigmentPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("pigment").forGetter(z -> z.pigment),
            Codec.LONG.fieldOf("amount").forGetter(z -> z.amount)
    ).apply(x, MekPigmentPortState::new));

    private final String pigment;
    private final long amount;

    public MekPigmentPortState(String gas, long amount) {
        this.pigment = gas;
        this.amount = amount;
        renderer = new ChemicalStackRenderer<>(amount, 16, 16);
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {
        long current = amount;
        for (PortStorage st : storage) {
            if (st instanceof MekPigmentPortStorage) {
                MekPigmentPortStorage gasStorage = (MekPigmentPortStorage) st;
                if (gasStorage.getInv().getStack().getType().getRegistryName().toString().equals(pigment)) {
                    PigmentStack extract = gasStorage.getInv().extractChemical(0, current, Action.EXECUTE);
                    current -= extract.getAmount();
                }
                if (current <= 0){
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateRequirement(List<PortStorage> storage) {
        long current = amount;
        for (PortStorage st : storage) {
            if (st instanceof MekPigmentPortStorage) {
                MekPigmentPortStorage gasStorage = (MekPigmentPortStorage) st;
                if (gasStorage.getInv().getStack().getType().getRegistryName().toString().equals(pigment)) {
                    PigmentStack extract = gasStorage.getInv().extractChemical(0, current, Action.SIMULATE);
                    current -= extract.getAmount();
                }
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {
        long current = amount;
        for (PortStorage st : storage) {
            if (st instanceof MekPigmentPortStorage) {
                MekPigmentPortStorage gasStorage = (MekPigmentPortStorage) st;
                PigmentStack extract = gasStorage.getInv().insertChemical(new PigmentStack(Objects.requireNonNull(MekanismAPI.pigmentRegistry().getValue(RLUtils.toRL(pigment))), current), Action.EXECUTE);
                current -= current - extract.getAmount();
                if (current <= 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        long current = amount;
        for (PortStorage st : storage) {
            if (st instanceof MekPigmentPortStorage) {
                MekPigmentPortStorage gasStorage = (MekPigmentPortStorage) st;
                PigmentStack extract = gasStorage.getInv().insertChemical(new PigmentStack(Objects.requireNonNull(MekanismAPI.pigmentRegistry().getValue(RLUtils.toRL(pigment))), current), Action.SIMULATE);
                current -= current - extract.getAmount();
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "mekanism_pigment");
    }

    @SneakyThrows
    @Override
    public void validateDefinition() {
        if (!RLUtils.isRL(pigment)){
            throw new InvalidProcessDefinitionException("Pigment Type: " + pigment + " is not a valid pigment type id (ResourceLocation)");
        }

        if (!MekanismAPI.pigmentRegistry().containsKey(RLUtils.toRL(pigment))){
            throw new InvalidProcessDefinitionException("Pigment Type: " + pigment + " does not exist in the mekansim pigment registry");
        }
    }

    private final ChemicalStackRenderer<PigmentStack> renderer;
    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic drawable = helpers.getGuiHelper().getSlotDrawable();
        drawable.draw(ms, x, y);
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        IGuiIngredientGroup<PigmentStack> gasGroup = layout.getIngredientsGroup(MekanismJEI.TYPE_PIGMENT);
        gasGroup.init(typeIndex, input, renderer, x + 1,  y + 1, 16, 16, 0, 0);
        gasGroup.set(typeIndex, new PigmentStack(MekanismAPI.pigmentRegistry().getValue(RLUtils.toRL(pigment)), amount));
        if (this.getChance() < 1) {
            gasGroup.addTooltipCallback((s, a, b, c) -> {
                if (s == typeIndex) {
                    c.add(new StringTextComponent("Chance: " + this.getChance() * 100 + "%"));
                }
            });
        }
    }

    @Override
    public <T> List<T> getIngredient(boolean input) {
        return (List<T>) ImmutableList.of(new PigmentStack(MekanismAPI.pigmentRegistry().getValue(RLUtils.toRL(pigment)), amount));
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return MekanismJEI.TYPE_PIGMENT;
    }
}
