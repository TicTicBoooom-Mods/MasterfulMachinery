package com.ticticboooom.mods.mm.client.jei.ingredients.pressure;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import lombok.Setter;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class PNCPressureIngredientRenderer implements IIngredientRenderer<PressureStack> {
    @Setter
    private IJeiHelpers helpers;

    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable PressureStack ingredient) {
        if (ingredient != null) {
            IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 1, 62, 16, 16);
            drawable.draw(matrixStack, xPosition, yPosition);
        }
    }

    @Override
    public List<ITextComponent> getTooltip(PressureStack ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
            new StringTextComponent("PNC Pressure"),
                new StringTextComponent(ingredient.getAmount() + " Units")
        );
    }
}
