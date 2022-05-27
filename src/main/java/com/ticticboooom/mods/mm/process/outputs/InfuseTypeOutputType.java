package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.infuse_type.InfuseTypePortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.util.ResourceLocation;

public class InfuseTypeOutputType extends ProcessOutputType {
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
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof InfuseTypePortStorage) {
                InfuseTypePortStorage infuseType = (InfuseTypePortStorage) output;
                    InfusionStack stackInSlot = infuseType.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.infuseType)) {
                        validCount -= infuseType.handler.getTankCapacity(0) - stackInSlot.getAmount();
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
        InfuseTypeValue eval = (InfuseTypeValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof InfuseTypePortStorage) {
                InfuseTypePortStorage infuseType = (InfuseTypePortStorage) output;
                    InfusionStack stackInSlot = infuseType.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.infuseType)) {
                        long diff = infuseType.handler.getTankCapacity(0) - stackInSlot.getAmount();
                        long relamount = Math.min(diff, validCount);
                        infuseType.handler.insertChemical(0, new InfusionStack(stackInSlot.getType(), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    } else if (stackInSlot.isEmpty()) {
                        long maxStackSize = infuseType.handler.getTankCapacity(0);
                        long relamount = Math.min(maxStackSize, validCount);
                        infuseType.handler.insertChemical(0, new InfusionStack(MekanismAPI.infuseTypeRegistry().getValue(eval.infuseType), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    }
                    if (validCount <= 0) {
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
