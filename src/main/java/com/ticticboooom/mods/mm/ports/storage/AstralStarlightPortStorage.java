package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class AstralStarlightPortStorage extends PortStorage {



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
        return null;
    }

    @Override
    public void load(CompoundNBT nbt) {

    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }

    @Override
    public void tick(MachinePortBlockEntity tile) {
    }
}
