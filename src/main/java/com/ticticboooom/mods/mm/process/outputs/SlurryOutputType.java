package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.slurry.SlurryPortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.util.ResourceLocation;

public class SlurryOutputType extends ProcessOutputType {
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
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof SlurryPortStorage) {
                SlurryPortStorage slurry = (SlurryPortStorage) output;
                    SlurryStack stackInSlot = slurry.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.slurry)) {
                        validCount -= slurry.handler.getTankCapacity(0) - stackInSlot.getAmount();
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
        SlurryValue eval = (SlurryValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof SlurryPortStorage) {
                SlurryPortStorage slurry = (SlurryPortStorage) output;
                    SlurryStack stackInSlot = slurry.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.slurry)) {
                        long diff = slurry.handler.getTankCapacity(0) - stackInSlot.getAmount();
                        long relamount = Math.min(diff, validCount);
                        slurry.handler.insertChemical(0, new SlurryStack(stackInSlot.getType(), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    } else if (stackInSlot.isEmpty()) {
                        long maxStackSize = slurry.handler.getTankCapacity(0);
                        long relamount = Math.min(maxStackSize, validCount);
                        slurry.handler.insertChemical(0, new SlurryStack(MekanismAPI.slurryRegistry().getValue(eval.slurry), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    }
                    if (validCount <= 0) {
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
