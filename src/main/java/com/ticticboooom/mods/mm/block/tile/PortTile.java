package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.ter.model.port.PortBlockModel;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.net.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class PortTile extends TileEntity implements ITickableTileEntity {
    public PortTile() {
        super(MMTiles.PORT.get());
    }

    public PortModel portModel;

    @Override
    public void tick() {
        if (EffectiveSide.get().isServer()) {
            CompoundNBT compound = write(new CompoundNBT());
            MMNetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(this.pos, compound));
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(PortBlockModel.PORT, portModel)
                .build();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("PortId")) {
            this.portModel = DataRegistry.PORTS.get(ResourceLocation.tryCreate(nbt.getString("PortId")));
        }
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (portModel != null && portModel.id != null) {
            compound.putString("PortId", portModel.id.toString());
        }
        return super.write(compound);
    }
}
