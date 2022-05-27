package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.infuse_type.InfuseTypePortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import mekanism.api.Action;
import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.util.ResourceLocation;

public class InfuseTypeIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        InfuseTypeValue infuseTypeValue = new InfuseTypeValue(this.getRegistryName());
        infuseTypeValue.infuseType = ParserUtils.parseOrDefault(object, "infuseType", x -> ResourceLocation.tryCreate(x.getAsString()), null);
        infuseTypeValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return infuseTypeValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        InfuseTypeValue eval = (InfuseTypeValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof InfuseTypePortStorage) {
                InfuseTypePortStorage infuseType = (InfuseTypePortStorage) input;
                InfusionStack stackInSlot = infuseType.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(eval.infuseType)) {
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
        InfuseTypeValue ival = (InfuseTypeValue) val;
        int countLeft = ival.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof InfuseTypePortStorage) {
                InfuseTypePortStorage infuseType = (InfuseTypePortStorage) input;
                InfusionStack stackInSlot = infuseType.handler.getChemicalInTank(0);
                if (stackInSlot.getType().getRegistryName().equals(ival.infuseType)) {
                    infuseType.handler.extractChemical(0, countLeft, Action.EXECUTE);
                    countLeft -= stackInSlot.getAmount();
                }
                if (countLeft <= 0) {
                    return;
                }
            }
        }

    }

    public static final class InfuseTypeValue extends Value {
        public ResourceLocation infuseType;
        public int amount;

        public InfuseTypeValue(ResourceLocation type) {
            super(type);
        }
    }
}
