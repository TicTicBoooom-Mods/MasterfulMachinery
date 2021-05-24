package com.ticticboooom.mods.mm.client.jei.ingredients;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.inventory.botania.PortManaInventory;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.List;

public class ManaIngredientRenderer implements IIngredientRenderer<PortManaInventory> {
    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable PortManaInventory ingredient) {

    }

    @Override
    public List<ITextComponent> getTooltip(PortManaInventory ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
            new StringTextComponent("Botania Mana"),
                new StringTextComponent(ingredient.getManaStored() + " Units")
        );
    }
}
