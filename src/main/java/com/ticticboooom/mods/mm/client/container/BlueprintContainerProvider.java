package com.ticticboooom.mods.mm.client.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class BlueprintContainerProvider implements INamedContainerProvider {
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.mm.blueprint");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new BlueprintContainer(p_createMenu_1_, p_createMenu_2_, null);
    }
}
