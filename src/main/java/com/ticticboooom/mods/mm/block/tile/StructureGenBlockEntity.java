package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.container.StructureGenBlockContainer;
import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import com.ticticboooom.mods.mm.registration.MMSetup;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class StructureGenBlockEntity extends UpdatableTile implements ITickableTileEntity, INamedContainerProvider {
    public StructureGenBlockEntity() {
        super(MMSetup.STRUCTURE_TILE.get());
    }


    @Getter
    private ItemStackHandler handler = new ItemStackHandler(1);
    @Getter
    private ItemStackInventory inv = new ItemStackInventory(handler);
    private ItemStack device = ItemStack.EMPTY;

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        NonNullList<ItemStack> list = NonNullList.from(device);
        nbt = ItemStackHelper.saveAllItems(nbt, list);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState p_230337_1_, CompoundNBT nbt) {
        NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, list);
        device = list.get(0);
        super.read(p_230337_1_, nbt);
    }


    @Override
    public void tick() {
        update();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Structure Generator");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new StructureGenBlockContainer(p_createMenu_1_, p_createMenu_2_, this);
    }
}
