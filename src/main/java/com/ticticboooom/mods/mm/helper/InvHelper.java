package com.ticticboooom.mods.mm.helper;

import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import net.minecraft.inventory.Inventory;
import net.minecraftforge.items.ItemStackHandler;

public class InvHelper {
    public static ItemStackInventory getItems(ItemStackHandler handler){
        return new ItemStackInventory(handler);
    }
}
