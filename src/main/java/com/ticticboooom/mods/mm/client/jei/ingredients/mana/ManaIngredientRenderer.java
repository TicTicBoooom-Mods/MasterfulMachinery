package com.ticticboooom.mods.mm.client.jei.ingredients.mana;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.inventory.botania.PortManaInventory;
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

public class ManaIngredientRenderer implements IIngredientRenderer<PortManaInventory> {
    @Setter
    private IJeiHelpers helpers;

    @Override
    public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable PortManaInventory ingredient) {
        if (ingredient != null) {
            IDrawableStatic drawable = helpers.getGuiHelper().createDrawable(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"), 19, 80, 16, 16);
            drawable.draw(matrixStack, xPosition, yPosition);
        }
    }

    @Override
    public List<ITextComponent> getTooltip(PortManaInventory ingredient, ITooltipFlag tooltipFlag) {
        return Lists.newArrayList(
            new StringTextComponent("Botania Mana"),
                new StringTextComponent(ingredient.getManaStored() + " Units")
        );
    }
}
