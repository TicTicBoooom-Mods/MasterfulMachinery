package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.mekanism.gas.GasPortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.util.ResourceLocation;

public class GasOutputType extends ProcessOutputType {
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
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof GasPortStorage) {
                GasPortStorage gas = (GasPortStorage) output;
                    GasStack stackInSlot = gas.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.gas)) {
                        validCount -= gas.handler.getTankCapacity(0) - stackInSlot.getAmount();
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
        GasValue eval = (GasValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof GasPortStorage) {
                GasPortStorage gas = (GasPortStorage) output;
                    GasStack stackInSlot = gas.handler.getChemicalInTank(0);
                    if (stackInSlot.getType().getRegistryName().equals(eval.gas)) {
                        long diff = gas.handler.getTankCapacity(0) - stackInSlot.getAmount();
                        long relamount = Math.min(diff, validCount);
                        gas.handler.insertChemical(0, new GasStack(stackInSlot.getType(), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    } else if (stackInSlot.isEmpty()) {
                        long maxStackSize = gas.handler.getTankCapacity(0);
                        long relamount = Math.min(maxStackSize, validCount);
                        gas.handler.insertChemical(0, new GasStack(MekanismAPI.gasRegistry().getValue(eval.gas), relamount), Action.EXECUTE);
                        validCount -= relamount;
                    }
                    if (validCount <= 0) {
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
