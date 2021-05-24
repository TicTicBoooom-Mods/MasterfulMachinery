package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class PortStorage {
    public abstract <T> LazyOptional<T> getLO();
    public abstract <T> boolean validate(Capability<T> cap);
    public abstract CompoundNBT save(CompoundNBT nbt);
    public abstract void load(CompoundNBT nbt);
    public abstract void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen);
    public void setupContainer(PortBlockContainer container, PlayerInventory inv, IMachinePortTile tile){
        int playerOffsetX = 8;
        int playerOffsetY = 141;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                container.addSlot(new Slot(inv, 9 + (j * 9 + i), i* 18 + playerOffsetX, j* 18 + playerOffsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            container.addSlot(new Slot(inv, i,8 + (i * 18), 199));
        }
    };

    public void tick(MachinePortBlockEntity tile) {

    }

    public void neighborChanged() {

    }
}
