package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.ter.model.port.PortBlockModel;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;

public class PortTile extends TileEntity implements ITickableTileEntity {
    public PortTile() {
        super(MMTiles.PORT.get());
    }

    public PortModel portModel;
    public boolean isInput;

    @Override
    public void tick() {
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(PortBlockModel.PORT, portModel)
                .withInitial(PortBlockModel.PORT_IO_TYPE, isInput)
                .build();
    }
}
