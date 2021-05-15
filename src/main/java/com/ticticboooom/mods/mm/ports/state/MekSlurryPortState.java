package com.ticticboooom.mods.mm.ports.state;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.exception.InvalidProcessDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.MekSlurryPortStorage;
import lombok.SneakyThrows;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.client.jei.ChemicalStackRenderer;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;
import java.util.Objects;

public class MekSlurryPortState extends PortState {

    public static final Codec<MekSlurryPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("slurry").forGetter(z -> z.slurry),
            Codec.LONG.fieldOf("amount").forGetter(z -> z.amount)
    ).apply(x, MekSlurryPortState::new));

    private final String slurry;
    private final long amount;

    public MekSlurryPortState(String gas, long amount) {
        this.slurry = gas;
        this.amount = amount;
        renderer = new ChemicalStackRenderer<>(amount, false,  16, 16, null);
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {
        long current = amount;
        for (PortStorage st : storage) {
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                if (gasStorage.getInv().getStack().getType().getRegistryName().toString().equals(slurry)) {
                    SlurryStack extract = gasStorage.getInv().extractChemical(0, current, Action.EXECUTE);
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
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                if (gasStorage.getInv().getStack().getType().getRegistryName().toString().equals(slurry)) {
                    SlurryStack extract = gasStorage.getInv().extractChemical(0, current, Action.SIMULATE);
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
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().insertChemical(new SlurryStack(Objects.requireNonNull(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry))), current), Action.EXECUTE);
                current -= extract.getAmount();
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
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().insertChemical(new SlurryStack(Objects.requireNonNull(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry))), current), Action.SIMULATE);
                current -= extract.getAmount();
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "mekanism_slurry");
    }

    @SneakyThrows
    @Override
    public void validateDefinition() {
        if (!RLUtils.isRL(slurry)){
            throw new InvalidProcessDefinitionException("Slurry: " + slurry + " is not a valid slurry id (ResourceLocation)");
        }

        if (!MekanismAPI.slurryRegistry().containsKey(RLUtils.toRL(slurry))){
            throw new InvalidProcessDefinitionException("Slurry: " + slurry + " does not exist in the mekansim slurry registry");
        }
    }

    private final ChemicalStackRenderer<SlurryStack> renderer;
    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic drawable = helpers.getGuiHelper().getSlotDrawable();
        drawable.draw(ms, x, y);
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        IGuiIngredientGroup<SlurryStack> gasGroup = layout.getIngredientsGroup(MekanismJEI.TYPE_SLURRY);
        gasGroup.init(typeIndex, input, renderer, x + 1,  y + 1, 16, 16, 0, 0);
        gasGroup.set(typeIndex, new SlurryStack(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry)), amount));
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
        return (List<T>) ImmutableList.of(new SlurryStack(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry)), amount));
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return MekanismJEI.TYPE_SLURRY;
    }
}
