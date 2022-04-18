package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.items.ItemPortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsOutputType extends ProcessOutputType {
    @Override
    public Value parse(JsonObject object) {
        ItemsValue itemsValue = new ItemsValue(this.getRegistryName());
        itemsValue.item = ParserUtils.parseOrDefault(object, "item", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        itemsValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return itemsValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        ItemsOutputType.ItemsValue ival = (ItemsOutputType.ItemsValue) val;
        int validCount = ival.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof ItemPortStorage) {
                ItemPortStorage items = (ItemPortStorage) output;
                for (int i = 0; i < items.itemStackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = items.itemStackHandler.getStackInSlot(i);
                    if (stackInSlot.getItem().getRegistryName().equals(ival.item)) {
                        validCount -= stackInSlot.getMaxStackSize() - stackInSlot.getCount();
                    } else if (stackInSlot.isEmpty()) {
                        validCount -= new ItemStack(ForgeRegistries.ITEMS.getValue(ival.item)).getMaxStackSize();
                    }
                }
                if (validCount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void process(Value val, MachineProcessContext ctx) {
        ItemsOutputType.ItemsValue ival = (ItemsOutputType.ItemsValue) val;
        int validCount = ival.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof ItemPortStorage) {
                ItemPortStorage items = (ItemPortStorage) output;
                for (int i = 0; i < items.itemStackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = items.itemStackHandler.getStackInSlot(i);
                    if (stackInSlot.getItem().getRegistryName().equals(ival.item)) {
                        int diff = stackInSlot.getMaxStackSize() - stackInSlot.getCount();
                        int relamount = Math.min(diff, validCount);
                        items.itemStackHandler.insertItem(i, new ItemStack(stackInSlot.getItem(), relamount), false);
                        validCount -= relamount;
                    } else if (stackInSlot.isEmpty()) {
                        int maxStackSize = new ItemStack(ForgeRegistries.ITEMS.getValue(ival.item)).getMaxStackSize();
                        int relamount = Math.min(maxStackSize, validCount);
                        items.itemStackHandler.insertItem(i, new ItemStack(ForgeRegistries.ITEMS.getValue(ival.item), relamount), false);
                        validCount -= relamount;
                    }
                    if (validCount <= 0) {
                        return;
                    }
                }
            }
        }
    }

    public static final class ItemsValue extends Value {
        public ResourceLocation item;
        public int amount;

        public ItemsValue(ResourceLocation type) {
            super(type);
        }
    }
}
