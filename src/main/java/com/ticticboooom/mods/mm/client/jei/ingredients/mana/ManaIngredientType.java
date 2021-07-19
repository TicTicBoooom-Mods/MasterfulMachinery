package com.ticticboooom.mods.mm.client.jei.ingredients.mana;

import com.ticticboooom.mods.mm.inventory.botania.PortManaInventory;
import mezz.jei.api.ingredients.IIngredientType;

public class ManaIngredientType implements IIngredientType<PortManaInventory> {
    @Override
    public Class<? extends PortManaInventory> getIngredientClass() {
        return PortManaInventory.class;
    }
}
