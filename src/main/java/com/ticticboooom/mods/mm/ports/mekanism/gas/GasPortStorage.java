package com.ticticboooom.mods.mm.ports.mekanism.gas;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class GasPortStorage extends PortStorage {

    private GasStack stack;
    private long capacity;

    public final LazyOptional<MMGasHandler> handlerLO;
    public final MMGasHandler handler;

    public GasPortStorage(long capacity) {
        this.capacity = capacity;
        this.handler = new MMGasHandler(capacity);
        handlerLO = LazyOptional.of(() -> this.handler);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != Capabilities.GAS_HANDLER_CAPABILITY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT gas = new CompoundNBT();
        gas.putLong("capacity",capacity);
        if (!handler.getChemicalInTank(0).isEmpty()){
            gas.putString("gas",handler.getChemicalInTank(0).getType().getRegistryName().toString());
            gas.putLong("amount",handler.getChemicalInTank(0).getAmount());
        }
        return gas;
    }

    @Override
    public void load(CompoundNBT nbt) {
        capacity = nbt.getLong("capacity");
        handler.setStack(new GasStack(Objects.requireNonNull(MekanismAPI.gasRegistry().getValue(ResourceLocation.tryCreate(nbt.getString("gas")))), nbt.getLong("amount")));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }
}
