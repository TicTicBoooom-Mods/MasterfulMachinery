package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.ter.model.controller.ControllerBlockModel;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;

public class ControllerTile extends TileEntity implements ITickableTileEntity {
    public ControllerTile() {
        super(MMTiles.CONTROLLER.get());
    }

    public ControllerModel controllerModel;

    @Override
    public void tick() {
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(ControllerBlockModel.CONTROLLER, controllerModel).build();
    }
}
