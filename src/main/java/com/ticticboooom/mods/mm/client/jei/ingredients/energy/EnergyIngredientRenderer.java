package com.ticticboooom.mods.mm.client.jei.ingredients.energy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.EnergyStack;
import lombok.Setter;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class EnergyIngredientRenderer implements IIngredientRenderer<EnergyStack>  {
    @Setter
    private IJeiHelpers helpers;
    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable EnergyStack ingredient) {
        if (ingredient != null) {
            IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 19, 62, 16, 16);
            drawable.draw(matrixStack, xPosition, yPosition);
        }
    }

    @Override
    public List<ITextComponent> getTooltip(EnergyStack ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
                new StringTextComponent("Forge Energy"),
                new StringTextComponent(NumberFormat.getInstance().format(ingredient.getAmount()) + " FE")
        );
    }
}
