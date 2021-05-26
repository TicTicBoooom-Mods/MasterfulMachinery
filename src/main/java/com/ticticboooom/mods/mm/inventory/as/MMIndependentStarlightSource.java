package com.ticticboooom.mods.mm.inventory.as;

import com.ticticboooom.mods.mm.block.tile.AstralMachinePortBlockEntity;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.constellation.Constellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.SourceClassRegistry;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class MMIndependentStarlightSource implements IIndependentStarlightSource {

    private StarlightPortStorage storage;
    IWeakConstellation attunedConstellation = null;


    @Override
    public float produceStarlightTick(ServerWorld serverWorld, BlockPos blockPos) {
        return storage.getInv().extractStarlight(Integer.MAX_VALUE, false);
    }

    @Nullable
    @Override
    public IWeakConstellation getStarlightType() {
        return ConstellationsAS.aevitas;
    }

    @Override
    public void threadedUpdateProximity(BlockPos blockPos, Map<BlockPos, IIndependentStarlightSource> map) {

    }

    @Override
    public SourceClassRegistry.SourceProvider getProvider() {
        return new Provider();
    }

    @Override
    public void readFromNBT(CompoundNBT compound) {
        this.attunedConstellation = NBTHelper.readOptional(compound, "constellation", (nbt) -> {
            IConstellation cst = IConstellation.readFromNBT(nbt);
            if (cst instanceof IWeakConstellation) {
                return (IWeakConstellation) cst;
            }
            return null;
        });
    }

    @Override
    public void writeToNBT(CompoundNBT compound) {
        NBTHelper.writeOptional(compound, "constellation", this.attunedConstellation, (nbt, cst) -> cst.writeToNBT(nbt));
    }

    @Override
    public <T extends TileEntity> boolean updateFromTileEntity(T tile) {
        if (tile instanceof AstralMachinePortBlockEntity) {
            AstralMachinePortBlockEntity astralTile = (AstralMachinePortBlockEntity) tile;
            attunedConstellation = astralTile.getAttunedConstellation();
            storage = (StarlightPortStorage) astralTile.getStorage();
        }
        return true;
    }

    public static class Provider implements SourceClassRegistry.SourceProvider {

        @Override
        public IIndependentStarlightSource provideEmptySource() {
            return new MMIndependentStarlightSource();
        }

        @Override
        @Nonnull
        public ResourceLocation getIdentifier() {
            return AstralSorcery.key("mm_independent_source");
        }

    }
}
