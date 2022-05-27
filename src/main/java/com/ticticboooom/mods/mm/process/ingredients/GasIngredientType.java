package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.gas.GasPortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import mekanism.api.Action;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GasIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        GasValue gasValue = new GasValue(this.getRegistryName());
        gasValue.gas = ParserUtils.parseOrDefault(object, "gas", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        gasValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return gasValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        GasValue eval = (GasValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof GasPortStorage) {
                GasPortStorage gas = (GasPortStorage) input;
                GasStack stackInSlot = gas.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(eval.gas)) {
                    validCount += stackInSlot.getAmount();
                }
                if (validCount >= eval.amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void process(Value val, MachineProcessContext ctx) {
        GasValue ival = (GasValue) val;
        int countLeft = ival.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof GasPortStorage) {
                GasPortStorage gas = (GasPortStorage) input;
                GasStack stackInSlot = gas.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(ival.gas)) {
                    gas.handler.extractChemical(0, countLeft, Action.EXECUTE);
                    countLeft -= stackInSlot.getAmount();
                }
                if (countLeft <= 0) {
                    return;
                }
            }
        }

    }

    public static final class GasValue extends Value {
        public ResourceLocation gas;
        public int amount;

        public GasValue(ResourceLocation type) {
            super(type);
        }
    }
}
