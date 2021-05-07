package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.inventory.PortEnergyInventory;
import lombok.Getter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyPortStorage implements IPortStorage {
    public static final Codec<EnergyPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.inv.getMaxEnergyStored())
    ).apply(x, EnergyPortStorage::new));

    @Getter
    private final PortEnergyInventory inv;
    private final LazyOptional<PortEnergyInventory> invLO;

    public EnergyPortStorage(int capacity) {
        this.inv = new PortEnergyInventory(0, capacity);
        invLO =  LazyOptional.of(() -> this.inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityEnergy.ENERGY;
    }
}
