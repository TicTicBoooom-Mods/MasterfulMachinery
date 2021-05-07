package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemPortStorage implements IPortStorage {
    public static final Codec<ItemPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("slots").forGetter(z -> z.inv.getSlots())
    ).apply(x, ItemPortStorage::new));

    @Getter
    private final ItemStackHandler inv;
    private final LazyOptional<ItemStackHandler> invLO;

    public ItemPortStorage(int slots) {
        this.inv = new ItemStackHandler(slots);
        this.invLO = LazyOptional.of(() -> this.inv);
    }


    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }
}
