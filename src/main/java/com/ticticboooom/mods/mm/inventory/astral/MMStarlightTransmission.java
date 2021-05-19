package com.ticticboooom.mods.mm.inventory.astral;

import hellfirepvp.astralsorcery.common.starlight.IStarlightTransmission;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimplePrismTransmissionNode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MMStarlightTransmission implements IStarlightTransmission<SimplePrismTransmissionNode> {

    private final World world;
    private final BlockPos pos;

    public MMStarlightTransmission(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    @Nonnull
    @Override
    public BlockPos getTrPos() {
        return pos;
    }

    @Nonnull
    @Override
    public World getTrWorld() {
        return world;
    }

    @Nonnull
    @Override
    public SimplePrismTransmissionNode provideTransmissionNode(BlockPos blockPos) {
        return new SimplePrismTransmissionNode(blockPos);
    }
}
