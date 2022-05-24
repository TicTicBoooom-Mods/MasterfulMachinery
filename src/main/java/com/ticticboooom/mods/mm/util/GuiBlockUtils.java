package com.ticticboooom.mods.mm.util;

import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.client.helper.AirBlockReader;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiBlockUtils {
    public static GuiBlockRenderBuilder getGuiBlockPort(BlockPos pos, ResourceLocation id){
        BlockState defaultState = MMBlocks.PORT.get().getDefaultState();
        AirBlockReader airBlockReader = new AirBlockReader(defaultState);
        TileEntity tile = defaultState.createTileEntity(airBlockReader);
        PortTile ptile = tile instanceof PortTile ? ((PortTile) tile) : null;
        ptile.portModel = DataRegistry.PORTS.get(id);
        return new GuiBlockRenderBuilder(defaultState, ptile, airBlockReader).at(pos);
    }

    public static GuiBlockRenderBuilder getGuiBlockController(BlockPos pos, ResourceLocation id){
        BlockState defaultState = MMBlocks.CONTROLLER.get().getDefaultState();
        AirBlockReader airBlockReader = new AirBlockReader(defaultState);
        TileEntity tile = defaultState.createTileEntity(airBlockReader);
        ControllerTile ptile = tile instanceof ControllerTile ? ((ControllerTile) tile) : null;
        ptile.controllerModel = DataRegistry.CONTROLLERS.get(id);
        return new GuiBlockRenderBuilder(defaultState, ptile, airBlockReader).at(pos);
    }
}
