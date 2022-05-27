package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.pigment.PigmentPortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import mekanism.api.Action;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.util.ResourceLocation;

public class PigmentIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        PigmentValue pigmentValue = new PigmentValue(this.getRegistryName());
        pigmentValue.pigment = ParserUtils.parseOrDefault(object, "pigment", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        pigmentValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return pigmentValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        PigmentValue eval = (PigmentValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof PigmentPortStorage) {
                PigmentPortStorage pigment = (PigmentPortStorage) input;
                PigmentStack stackInSlot = pigment.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(eval.pigment)) {
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
        PigmentValue ival = (PigmentValue) val;
        int countLeft = ival.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof PigmentPortStorage) {
                PigmentPortStorage pigment = (PigmentPortStorage) input;
                PigmentStack stackInSlot = pigment.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(ival.pigment)) {
                    pigment.handler.extractChemical(0, countLeft, Action.EXECUTE);
                    countLeft -= stackInSlot.getAmount();
                }
                if (countLeft <= 0) {
                    return;
                }
            }
        }

    }

    public static final class PigmentValue extends Value {
        public ResourceLocation pigment;
        public int amount;

        public PigmentValue(ResourceLocation type) {
            super(type);
        }
    }
}
