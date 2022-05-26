package com.ticticboooom.mods.mm.process.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.energy.EnergyPortStorage;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import net.minecraft.util.ResourceLocation;

public class EnergyIngredientType extends ProcessIngredientType {
    @Override
    public Value parse(JsonObject object) {
        EnergyValue energyValue = new EnergyValue(this.getRegistryName());
        energyValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return energyValue;
    }

    @Override
    public boolean canProcess(ProcessIngredientType.Value val, MachineProcessContext ctx) {
        EnergyValue eval = (EnergyValue) val;
        int validCount = 0;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof EnergyPortStorage) {
               EnergyPortStorage energy = (EnergyPortStorage) input;
                int amount = eval.amount;
                validCount += amount;
                if (validCount >= eval.amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void process(Value val, MachineProcessContext ctx) {
        EnergyValue eval = (EnergyValue) val;
        int countLeft = eval.amount;
        for (PortStorage input : ctx.inputs) {
            if (input instanceof EnergyPortStorage) {
                EnergyPortStorage energy = (EnergyPortStorage) input;
                countLeft -= energy.handler.getEnergyStored();
                energy.handler.extractEnergy(countLeft,false);
                    if (countLeft <= 0) {
                        return;
                    }
                }
            }
        }


    public static final class EnergyValue extends ProcessIngredientType.Value {
        public int amount;

        public EnergyValue(ResourceLocation type) {
        super(type);
    }
    }
}
