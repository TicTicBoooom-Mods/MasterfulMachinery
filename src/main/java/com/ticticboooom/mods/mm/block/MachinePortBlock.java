package com.ticticboooom.mods.mm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class MachinePortBlock extends Block {
    private RegistryObject<TileEntityType<?>> type;

    public MachinePortBlock(RegistryObject<TileEntityType<?>> type) {
        super(AbstractBlock.Properties.of(Material.METAL));
        this.type = type;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return type.get().create();
    }
}
