package com.ticticboooom.mods.mm.ports.mekanism.slurry;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class SlurryPortStorage extends PortStorage {

    private SlurryStack stack;
    private long capacity;

    public final LazyOptional<MMSlurryHandler> handlerLO;
    public final MMSlurryHandler handler;

    public SlurryPortStorage(long capacity) {
        this.capacity = capacity;
        this.handler = new MMSlurryHandler(capacity);
        handlerLO = LazyOptional.of(() -> this.handler);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != Capabilities.SLURRY_HANDLER_CAPABILITY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT slurry = new CompoundNBT();
        slurry.putLong("capacity",capacity);
        if (!handler.getChemicalInTank(0).isEmpty()){
            slurry.putString("slurry",handler.getChemicalInTank(0).getType().getRegistryName().toString());
            slurry.putLong("amount",handler.getChemicalInTank(0).getAmount());
        }
        return slurry;
    }

    @Override
    public void load(CompoundNBT nbt) {
        capacity = nbt.getLong("capacity");
        handler.setStack(new SlurryStack(Objects.requireNonNull(MekanismAPI.slurryRegistry().getValue(ResourceLocation.tryCreate(nbt.getString("slurry")))), nbt.getLong("amount")));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }
}
