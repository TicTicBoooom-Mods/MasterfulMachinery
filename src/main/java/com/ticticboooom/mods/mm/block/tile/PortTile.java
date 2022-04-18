package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.ter.model.port.PortBlockModel;
import com.ticticboooom.mods.mm.client.container.PortContainer;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.net.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.ports.PortTypeRegistry;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PortTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public PortTile() {
        super(MMTiles.PORT.get());
    }

    public PortModel portModel;
    public PortStorage storage;

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
            ResourceLocation type = this.portModel.type;
            PortType portType = PortTypeRegistry.PORT_TYPES.get(type);
            this.storage = portType.parseStorage(this.portModel.json);
            if (nbt.contains("storage")) {
                storage.load(nbt.getCompound("storage"));
            }
        }
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (portModel != null && portModel.id != null) {
            compound.putString("PortId", portModel.id.toString());
            compound.put("storage", storage.save(compound));
        }
        return super.write(compound);
    }

    @Override
    public ITextComponent getDisplayName() {
        return portModel.name;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new PortContainer(this, p_createMenu_2_, p_createMenu_1_);
    }


}
