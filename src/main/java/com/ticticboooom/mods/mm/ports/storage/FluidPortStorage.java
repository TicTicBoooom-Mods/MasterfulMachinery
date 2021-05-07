package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.inventory.PortFluidInventory;
import lombok.Getter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidPortStorage implements IPortStorage {
    public static final Codec<FluidPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.inv.getTankCapacity(0))
    ).apply(x, FluidPortStorage::new));


    @Getter
    private final PortFluidInventory inv;
    private final LazyOptional<PortFluidInventory> invLO;

    public FluidPortStorage(int capacity) {
        this.inv = new PortFluidInventory(capacity);
        invLO =  LazyOptional.of(() -> this.inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }
}
