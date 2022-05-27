package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.slurry.SlurryPortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import mekanism.api.Action;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.util.ResourceLocation;

public class SlurryIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        SlurryValue slurryValue = new SlurryValue(this.getRegistryName());
        slurryValue.slurry = ParserUtils.parseOrDefault(object, "slurry", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        slurryValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return slurryValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        SlurryValue eval = (SlurryValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof SlurryPortStorage) {
                SlurryPortStorage slurry = (SlurryPortStorage) input;
                SlurryStack stackInSlot = slurry.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(eval.slurry)) {
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
        SlurryValue ival = (SlurryValue) val;
        int countLeft = ival.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof SlurryPortStorage) {
                SlurryPortStorage slurry = (SlurryPortStorage) input;
                SlurryStack stackInSlot = slurry.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(ival.slurry)) {
                    slurry.handler.extractChemical(0, countLeft, Action.EXECUTE);
                    countLeft -= stackInSlot.getAmount();
                }
                if (countLeft <= 0) {
                    return;
                }
            }
        }

    }

    public static final class SlurryValue extends Value {
        public ResourceLocation slurry;
        public int amount;

        public SlurryValue(ResourceLocation type) {
            super(type);
        }
    }
}
