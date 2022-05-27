package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.pigment.PigmentPortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.util.ResourceLocation;

public class PigmentOutputType extends ProcessOutputType {
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
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof PigmentPortStorage) {
                PigmentPortStorage pigment = (PigmentPortStorage) output;
                    PigmentStack stackInSlot = pigment.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.pigment)) {
                        validCount -= pigment.handler.getTankCapacity(0) - stackInSlot.getAmount();
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
        PigmentValue eval = (PigmentValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof PigmentPortStorage) {
                PigmentPortStorage pigment = (PigmentPortStorage) output;
                    PigmentStack stackInSlot = pigment.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.pigment)) {
                        long diff = pigment.handler.getTankCapacity(0) - stackInSlot.getAmount();
                        long relamount = Math.min(diff, validCount);
                        pigment.handler.insertChemical(0, new PigmentStack(stackInSlot.getType(), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    } else if (stackInSlot.isEmpty()) {
                        long maxStackSize = pigment.handler.getTankCapacity(0);
                        long relamount = Math.min(maxStackSize, validCount);
                        pigment.handler.insertChemical(0, new PigmentStack(MekanismAPI.pigmentRegistry().getValue(eval.pigment), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    }
                    if (validCount <= 0) {
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
