package com.ticticboooom.mods.mm.integration.jei.ingredients.controller;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class ControllerIngredientRenderer implements IIngredientRenderer<ControllerModel> {

    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable ControllerModel ingredient) {
        BlockState state = ingredient.defaultModel.createState();
        matrixStack.push();
        Minecraft.getInstance().getItemRenderer().renderItemIntoGUI(new ItemStack(state.getBlock().asItem()), xPosition, yPosition);
        matrixStack.pop();
    }

    @Override
    public List<ITextComponent> getTooltip(ControllerModel ingredient, ITooltipFlag tooltipFlag) {
        return ImmutableList.of(ingredient.name);
    }
}
