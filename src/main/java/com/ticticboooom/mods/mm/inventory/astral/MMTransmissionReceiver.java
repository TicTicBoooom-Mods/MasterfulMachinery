package com.ticticboooom.mods.mm.inventory.astral;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MMTransmissionReceiver implements ITransmissionReceiver {

    private BlockPos pos;
    private final Set<BlockPos> sourcesToThis = new HashSet<>();

    public MMTransmissionReceiver(BlockPos pos) {

        this.pos = pos;
    }

    @Override
    public void onStarlightReceive(World world, IWeakConstellation iWeakConstellation, double v) {

    }

    @Override
    public BlockPos getLocationPos() {
        return pos;
    }

    @Override
    public void notifySourceLink(World world, BlockPos blockPos) {
        sourcesToThis.add(blockPos);
    }

    @Override
    public void notifySourceUnlink(World world, BlockPos blockPos) {
        sourcesToThis.remove(blockPos);
    }

    @Override
    public boolean notifyBlockChange(World world, BlockPos blockPos) {
        return false;
    }

    @Override
    public List<BlockPos> getSources() {
        return new LinkedList<>(sourcesToThis);
    }

    @Override
    public TransmissionProvider getProvider() {
        return null;
    }

    @Override
    public void readFromNBT(CompoundNBT compoundNBT) {

    }

    @Override
    public void writeToNBT(CompoundNBT compoundNBT) {

    }
}
