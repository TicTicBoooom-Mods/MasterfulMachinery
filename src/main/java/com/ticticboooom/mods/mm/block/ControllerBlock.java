package com.ticticboooom.mods.mm.block;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.MultiLayerModel;

import javax.annotation.Nullable;

public class ControllerBlock extends Block {
    public ControllerBlock() {
        super(Properties.create(Material.IRON));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MMTiles.CONTROLLER.get().create();
    }
}
