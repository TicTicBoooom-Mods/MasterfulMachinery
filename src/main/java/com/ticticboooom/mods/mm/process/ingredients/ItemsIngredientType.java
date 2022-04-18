package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.items.ItemPortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemsIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        ItemsValue itemsValue = new ItemsValue(this.getRegistryName());
        itemsValue.item = ParserUtils.parseOrDefault(object, "item", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        itemsValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return itemsValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        ItemsValue ival = (ItemsValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof ItemPortStorage) {
                ItemPortStorage items = (ItemPortStorage) input;
                for (int i = 0; i < items.itemStackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = items.itemStackHandler.getStackInSlot(i);
                    if (stackInSlot.getItem().getRegistryName().equals(ival.item)) {
                        validCount += stackInSlot.getCount();
                    }
                }
                if (validCount >= ival.amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void process(Value val, MachineProcessContext ctx) {
        ItemsValue ival = (ItemsValue) val;
        int countLeft = ival.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof ItemPortStorage) {
                ItemPortStorage items = (ItemPortStorage) input;
                for (int i = 0; i < items.itemStackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = items.itemStackHandler.getStackInSlot(i);
                    if (stackInSlot.getItem().getRegistryName().equals(ival.item)) {
                        items.itemStackHandler.extractItem(i, countLeft, false);
                        countLeft -= stackInSlot.getCount();
                    }
                    if (countLeft <= 0) {
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
