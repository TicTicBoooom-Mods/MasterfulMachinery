package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.ports.storage.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class EnergyPortState extends PortState {

public static final Codec<EnergyPortState> CODEC  =RecordCodecBuilder.create(x -> x.group(
                Codec.INT.fieldOf("amount").forGetter(z -> z.amount)
            ).apply(x, EnergyPortState::new));


    @Getter
    private final int amount;

    public EnergyPortState(int amount) {
        this.amount = amount;
    }

    @Override
    public void processRequirement(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().extractEnergy(current, false);
                if (current <= 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateRequirement(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().extractEnergy(current, true);
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().receiveEnergy(current, false);
                if (current <= 0) {
                    return;
                }
            }
        }
    }

    @Override
    public boolean validateResult(List<IPortStorage> storage) {
        int current = amount;
        for (IPortStorage inv : storage) {
            if (inv instanceof EnergyPortStorage) {
                EnergyPortStorage energyInv = (EnergyPortStorage) inv;
                current -= energyInv.getInv().receiveEnergy(current, true);
                if (current <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "energy");
    }
}
