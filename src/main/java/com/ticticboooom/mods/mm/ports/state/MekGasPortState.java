package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.ports.storage.MekGasPortStorage;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.inventory.AutomationType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

public class MekGasPortState extends PortState {

    public static final Codec<MekGasPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("gas").forGetter(z -> z.gas),
            Codec.LONG.fieldOf("amount").forGetter(z -> z.amount)
    ).apply(x, MekGasPortState::new));

    private final String gas;
    private final long amount;

    public MekGasPortState(String gas, long amount) {

        this.gas = gas;
        this.amount = amount;
    }

    @Override
    public void processRequirement(List<IPortStorage> storage) {
        long current = amount;
        for (IPortStorage st : storage) {
            if (st instanceof MekGasPortStorage) {
                MekGasPortStorage gasStorage = (MekGasPortStorage) st;
                GasStack extract = gasStorage.getInv().extract(current, Action.EXECUTE, AutomationType.EXTERNAL);
                current -= extract.getAmount();
            }
        }
    }

    @Override
    public boolean validateRequirement(List<IPortStorage> storage) {
        long current = amount;
        for (IPortStorage st : storage) {
            if (st instanceof MekGasPortStorage) {
                MekGasPortStorage gasStorage = (MekGasPortStorage) st;
                GasStack extract = gasStorage.getInv().extract(current, Action.SIMULATE, AutomationType.EXTERNAL);
                current -= extract.getAmount();
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<IPortStorage> storage) {
        long current = 0;
        for (IPortStorage st : storage) {
            if (st instanceof MekGasPortStorage) {
                MekGasPortStorage gasStorage = (MekGasPortStorage) st;
                GasStack extract = gasStorage.getInv().insertChemical(new GasStack(Objects.requireNonNull(MekanismAPI.gasRegistry().getValue(RLUtils.toRL(gas))), current), Action.EXECUTE);
                current += extract.getAmount();
            }
        }
    }

    @Override
    public boolean validateResult(List<IPortStorage> storage) {
        long current = 0;
        for (IPortStorage st : storage) {
            if (st instanceof MekGasPortStorage) {
                MekGasPortStorage gasStorage = (MekGasPortStorage) st;
                GasStack extract = gasStorage.getInv().insertChemical(new GasStack(Objects.requireNonNull(MekanismAPI.gasRegistry().getValue(RLUtils.toRL(gas))), current), Action.EXECUTE);
                current += extract.getAmount();
                if (current >= amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "mekanism_gas");
    }
}
