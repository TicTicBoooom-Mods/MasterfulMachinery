package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.Getter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class MachinePortBlockEntity extends TileEntity {

    @Getter
    private IPortStorage storage;
    @Getter
    private boolean input;


    public MachinePortBlockEntity(TileEntityType<?> p_i48289_1_, IPortStorage storage, boolean input) {
        super(p_i48289_1_);
        this.storage = storage;
        this.input = input;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (storage.validate(cap)){
            return storage.getLO();
        }
        return super.getCapability(cap, side);
    }
}
