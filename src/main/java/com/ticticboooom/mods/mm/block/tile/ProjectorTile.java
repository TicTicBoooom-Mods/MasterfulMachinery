package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.client.container.ProjectorContainer;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class ProjectorTile extends TileEntity implements INamedContainerProvider {

    public ProjectorTile() {
        super(MMTiles.PROJECTOR.get());
    }

    public Inventory blueprint = new Inventory(ItemStack.EMPTY);


    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Projector");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ProjectorContainer(this, p_createMenu_2_, p_createMenu_1_);
    }
}
