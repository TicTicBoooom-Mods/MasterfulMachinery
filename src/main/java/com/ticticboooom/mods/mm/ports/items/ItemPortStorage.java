package com.ticticboooom.mods.mm.ports.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.client.container.PortContainer;
import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemPortStorage extends PortStorage {
    private final int rows;
    private final int columns;

    public final LazyOptional<ItemStackHandler> itemStackHandlerLO;
    public final ItemStackHandler itemStackHandler;

    public ItemPortStorage(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.itemStackHandler = new ItemStackHandler(this.rows * this.columns);
        itemStackHandlerLO = LazyOptional.of(() -> this.itemStackHandler);
    }


    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemStackHandlerLO.cast();
        }
        return null;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put("items", itemStackHandler.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void load(CompoundNBT nbt) {
        CompoundNBT items = nbt.getCompound("items");
        itemStackHandler.deserializeNBT(items);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Ref.MOD_ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top - 20, 0, 0, 175, 256);
        int offsetY = ((108 - (rows * 18)) / 2) + 10;
        int offsetX = ((162 - (columns * 18)) / 2)  + 7;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                screen.blit(stack, left + (x * 18) + offsetX, top + (y * 18) + offsetY, 175, 256, 18, 18);
            }
        }
    }

    @Override
    public void setupContainer(PortContainer container, PlayerInventory inv, TileEntity tile) {
        int offsetX = ((162 - (columns * 18)) / 2) + 8;
        int offsetY = ((108 - (rows * 18)) / 2) + 11;
        ItemStackInventory items = new ItemStackInventory((ItemStackHandler) ((PortTile) tile).storage.getLazyOptional(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null));
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                container.addSlot(new Slot(items, (y * columns) + x, x * 18 + offsetX, y * 18 + offsetY));
            }
        }
        super.setupContainer(container, inv, tile);
    }
}
