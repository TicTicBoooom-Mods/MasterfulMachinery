package com.ticticboooom.mods.mm.process.outputs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.energy.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.items.ItemPortStorage;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class EnergyOutputType extends ProcessOutputType {
    @Override
    public Value parse(JsonObject object) {
        EnergyValue itemsValue = new EnergyValue(this.getRegistryName());
        itemsValue.amount = ParserUtils.parseOrDefault(object, "amount", JsonElement::getAsInt, 1);
        return itemsValue;
    }

    @Override
    public boolean canProcess(Value val, MachineProcessContext ctx) {
        EnergyOutputType.EnergyValue eval = (EnergyOutputType.EnergyValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof EnergyPortStorage) {
                EnergyPortStorage energy = (EnergyPortStorage) output;
                validCount -= (energy.handler.getMaxEnergyStored() - energy.handler.getEnergyStored());
                if (validCount <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void process(Value val, MachineProcessContext ctx) {
        EnergyOutputType.EnergyValue eval = (EnergyOutputType.EnergyValue) val;
        int validCount = eval.amount;
        for (PortStorage output : ctx.outputs) {
            if (output instanceof EnergyPortStorage) {
                EnergyPortStorage energy = (EnergyPortStorage) output;
                validCount -= (energy.handler.getMaxEnergyStored() - energy.handler.getEnergyStored());
                energy.handler.receiveEnergy(validCount,false);
                if (validCount <= 0) {
                    return;
                }
            }
        }
    }


    public static final class EnergyValue extends Value {
        public int amount;

        public EnergyValue(ResourceLocation type) {
            super(type);
        }
    }
}
