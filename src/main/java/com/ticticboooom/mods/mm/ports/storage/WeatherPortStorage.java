package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class WeatherPortStorage extends PortStorage {

    @Getter
    private boolean isRaining = false;
    @Getter
    private boolean isThundering = false;


    @Override
    public <T> LazyOptional<T> getLO() {
        return null;
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return false;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        return new CompoundNBT();
    }

    @Override
    public void load(CompoundNBT nbt) {

    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }

    @Override
    public void tick(IMachinePortTile tile) {
        TileEntity te = (TileEntity) tile;
        World world = te.getWorld();
        if (world instanceof ServerWorld){
            ServerWorld sWorld = (ServerWorld) world;
            isRaining = sWorld.getWorldInfo().isRaining();
            isThundering = sWorld.getWorldInfo().isThundering();
        }
    }
}
