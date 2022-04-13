package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MachinePortBlockEntity extends UpdatableTile implements ITickableTileEntity, IMachinePortTile {

    protected ContainerType<?> container;
    @Getter
    protected PortStorage storage;
    @Getter
    protected boolean input;


    public MachinePortBlockEntity(TileEntityType<?> p_i48289_1_, ContainerType<?> container, PortStorage storage, boolean input) {
        super(p_i48289_1_);
        this.container = container;
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

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("inv", storage.save(new CompoundNBT()));
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        storage.load(nbt.getCompound("inv"));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Port");
    }

    @Nullable
    @Override
    public Container createMenu(int windowsId, PlayerInventory inv, PlayerEntity player) {
        return new PortBlockContainer(container, windowsId, inv,this);
    }

    @Override
    public void tick() {
        if (world.isRemote()){
            return;
        }
        this.getStorage().tick(this);
        update();
    }
}
