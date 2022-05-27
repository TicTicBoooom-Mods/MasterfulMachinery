package com.ticticboooom.mods.mm.ports.mekanism.pigment;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class PigmentPortStorage extends PortStorage {

    private InfusionStack stack;
    private long capacity;

    public final LazyOptional<MMPigmentHandler> handlerLO;
    public final MMPigmentHandler handler;

    public PigmentPortStorage(long capacity) {
        this.capacity = capacity;
        this.handler = new MMPigmentHandler(capacity);
        handlerLO = LazyOptional.of(() -> this.handler);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != Capabilities.PIGMENT_HANDLER_CAPABILITY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT infusion = new CompoundNBT();
        infusion.putLong("capacity",capacity);
        if (!handler.getChemicalInTank(0).isEmpty()){
            infusion.putString("pigment",handler.getChemicalInTank(0).getType().getRegistryName().toString());
            infusion.putLong("amount",handler.getChemicalInTank(0).getAmount());
        }
        return infusion;
    }

    @Override
    public void load(CompoundNBT nbt) {
        capacity = nbt.getLong("capacity");
        handler.setStack(new PigmentStack(Objects.requireNonNull(MekanismAPI.pigmentRegistry().getValue(ResourceLocation.tryCreate(nbt.getString("infusion")))), nbt.getLong("amount")));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }
}
