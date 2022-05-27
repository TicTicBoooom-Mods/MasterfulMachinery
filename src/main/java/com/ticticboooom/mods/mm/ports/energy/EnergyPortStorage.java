package com.ticticboooom.mods.mm.ports.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.util.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyPortStorage extends PortStorage {

    private int stored,capacity;
    public MMEnergyHandler handler;
    public LazyOptional<MMEnergyHandler> handlerLO = LazyOptional.of(() -> handler);

    public EnergyPortStorage(int capacity) {
        this.capacity = capacity;
        handler = new MMEnergyHandler(stored,capacity);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != CapabilityEnergy.ENERGY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("stored", handler.getEnergyStored());
        return compoundNBT;
    }

    @Override
    public void load(CompoundNBT nbt) {
        if (nbt.contains("stored")){
            handler.setStored(nbt.getInt("stored"));
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Ref.MOD_ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top - 20, 0, 0, 175, 256);
        int x = 78;
        int y = 20;
        screen.blit(stack, left + x, top + y, 193, 18, 18, 108);
        int max = handler.getMaxEnergyStored();
        float pct = 0;
        if (max > 0) {
            pct = (float)handler.getEnergyStored() / max;
        }
        GuiHelper.renderVerticallyFilledBar(stack,screen,x,y,193,18,18,193,pct);
        AbstractGui.drawCenteredString(stack, Minecraft.getInstance().fontRenderer, handler.getEnergyStored() + "/" + max + " FE (" + String.format("%.2f",Math.round((float)10000 * pct) / 100.f) + "%)", left + x + 9 + 1, top + y + 30, 0xfefefe);
    }
}
