package com.ticticboooom.mods.mm.client.jei.ingredients;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class PNCPressureIngredientRenderer implements IIngredientRenderer<PressureStack> {
    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable PressureStack ingredient) {

    }

    @Override
    public List<ITextComponent> getTooltip(PressureStack ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
            new StringTextComponent("PNC Pressure"),
                new StringTextComponent(ingredient.getAmount() + "Units")
        );
    }
}
