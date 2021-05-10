package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.ports.storage.MekSlurryPortStorage;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.inventory.AutomationType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Objects;

public class MekSlurryPortState implements IPortState {

    public static final Codec<MekSlurryPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.fieldOf("slurry").forGetter(z -> z.slurry),
            Codec.LONG.fieldOf("amount").forGetter(z -> z.amount)
    ).apply(x, MekSlurryPortState::new));

    private final String slurry;
    private final long amount;

    public MekSlurryPortState(String gas, long amount) {

        this.slurry = gas;
        this.amount = amount;
    }

    @Override
    public void processRequirement(List<IPortStorage> storage) {
        long current = amount;
        for (IPortStorage st : storage) {
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().extract(current, Action.EXECUTE, AutomationType.EXTERNAL);
                current -= extract.getAmount();
            }
        }
    }

    @Override
    public boolean validateRequirement(List<IPortStorage> storage) {
        long current = amount;
        for (IPortStorage st : storage) {
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().extract(current, Action.SIMULATE, AutomationType.EXTERNAL);
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
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().insertChemical(new SlurryStack(Objects.requireNonNull(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry))), current), Action.EXECUTE);
                current += extract.getAmount();
            }
        }
    }

    @Override
    public boolean validateResult(List<IPortStorage> storage) {
        long current = 0;
        for (IPortStorage st : storage) {
            if (st instanceof MekSlurryPortStorage) {
                MekSlurryPortStorage gasStorage = (MekSlurryPortStorage) st;
                SlurryStack extract = gasStorage.getInv().insertChemical(new SlurryStack(Objects.requireNonNull(MekanismAPI.slurryRegistry().getValue(RLUtils.toRL(slurry))), current), Action.EXECUTE);
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
        return new ResourceLocation(MM.ID, "mekanism_slurry");
    }
}
