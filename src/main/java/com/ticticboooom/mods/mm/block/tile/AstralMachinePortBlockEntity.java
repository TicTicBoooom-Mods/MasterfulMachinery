package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.inventory.as.MMIndependentStarlightSource;
import com.ticticboooom.mods.mm.network.PacketHandler;
import com.ticticboooom.mods.mm.network.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import hellfirepvp.astralsorcery.common.constellation.ConstellationTile;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IIndependentStarlightSource;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionSourceNode;
import hellfirepvp.astralsorcery.common.tile.base.network.TileSourceBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AstralMachinePortBlockEntity extends TileSourceBase<SimpleTransmissionSourceNode> implements IMachinePortTile, ConstellationTile {
    public AstralMachinePortBlockEntity(TileEntityType<?> tileEntityTypeIn, ContainerType<?> cont,  PortStorage storage) {
        super(tileEntityTypeIn);
        this.cont = cont;
        this.storage = storage;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote()) {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(pos, write(new CompoundNBT())));
        }
    }

    private ContainerType<?> cont;
    PortStorage storage;
    IWeakConstellation constellationType = null;
    IMinorConstellation traitConstellationType = null;

    @Nonnull
    @Override
    public IIndependentStarlightSource provideNewSourceNode() {
        return new MMIndependentStarlightSource();
    }

    @Nonnull
    @Override
    public SimpleTransmissionSourceNode provideSourceNode(BlockPos blockPos) {
        return new SimpleTransmissionSourceNode(blockPos);
    }


    @Override
    public void writeCustomNBT(CompoundNBT nbt) {
        nbt.put("inv", storage.save(new CompoundNBT()));
        super.writeCustomNBT(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        storage.load(nbt.getCompound("inv"));
    }

    @Override
    public PortStorage getStorage() {
        return this.storage;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Port");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new PortBlockContainer(cont, p_createMenu_1_, p_createMenu_2_, this);
    }

    @Override
    public IWeakConstellation getAttunedConstellation() {
        return constellationType;
    }

    @Override
    public boolean setAttunedConstellation(IWeakConstellation cst) {
        if (cst != this.constellationType){
            markForUpdate();
        }
        constellationType = cst;
        return true;
    }

    @Override
    public IMinorConstellation getTraitConstellation() {
        return traitConstellationType;
    }

    @Override
    public boolean setTraitConstellation(IMinorConstellation tst) {
        if (tst != this.traitConstellationType){
            markForUpdate();
        }
        traitConstellationType = tst;
        return true;
    }
}
