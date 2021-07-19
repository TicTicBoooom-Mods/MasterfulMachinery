package com.ticticboooom.mods.mm.client.jei.ingredients.as;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.StarlightStack;
import lombok.Setter;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class StarlightIngredientRenderer implements IIngredientRenderer<StarlightStack> {
    @Setter
    private IJeiHelpers helpers;

    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable StarlightStack ingredient) {
        if (ingredient != null) {
            IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 37, 80, 16, 16);
            drawable.draw(matrixStack, xPosition, yPosition);
        }
    }

    @Override
    public List<ITextComponent> getTooltip(StarlightStack ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
            new StringTextComponent("Astral Starlight"),
                new StringTextComponent(ingredient.getAmount() + " S")
        );
    }
}
