package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.network.PacketHandler;
import com.ticticboooom.mods.mm.network.packets.TileClientUpdatePacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.network.PacketDistributor;

public class UpdatableTile extends TileEntity{
    public UpdatableTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public void update() {
        if (!level.isClientSide()){
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(worldPosition, save(new CompoundNBT())));
        }
    }
}
