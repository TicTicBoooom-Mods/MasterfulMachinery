package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.category.MMJeiPlugin;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.EnergyStack;
import com.ticticboooom.mods.mm.ports.storage.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.storage.ManaPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class ManaPortState extends PortState {

public static final Codec<ManaPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
                Codec.INT.fieldOf("amount").forGetter(z -> z.amount)
            ).apply(x, ManaPortState::new));


    @Getter
    private final int amount;

    public ManaPortState(int amount) {
        this.amount = amount;

    }

    @Override
    public void processRequirement(List<PortStorage> storage) {

    }

    @Override
    public boolean validateRequirement(List<PortStorage> storage) {
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {

    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "mana");
    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 18, 61, 18, 18);
        drawable.draw(ms, x, y);
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        IGuiIngredientGroup<EnergyStack> group = layout.getIngredientsGroup(MMJeiPlugin.ENERGY_TYPE);
        group.init(typeIndex, input, x + 1, y + 1);
        group.set(typeIndex, new EnergyStack(amount));
        if (this.getChance() < 1){
            group.addTooltipCallback((s, a, b, c) -> {
                if (s == typeIndex) {
                    c.add(new StringTextComponent("Chance: " + this.getChance() * 100 + "%"));
                }
            });
        }
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return MMJeiPlugin.ENERGY_TYPE;
    }
}