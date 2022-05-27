package com.ticticboooom.mods.mm.ports.mekanism.infuse_type;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Objects;

public class InfuseTypePortStorage extends PortStorage {

    private InfusionStack stack;
    private long capacity;

    public final LazyOptional<MMInfuseTypeHandler> handlerLO;
    public final MMInfuseTypeHandler handler;

    public InfuseTypePortStorage(long capacity) {
        this.capacity = capacity;
        this.handler = new MMInfuseTypeHandler(capacity);
        handlerLO = LazyOptional.of(() -> this.handler);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != Capabilities.INFUSION_HANDLER_CAPABILITY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT infusion = new CompoundNBT();
        infusion.putLong("capacity",capacity);
        if (!handler.getChemicalInTank(0).isEmpty()){
            infusion.putString("infusion",handler.getChemicalInTank(0).getType().getRegistryName().toString());
            infusion.putLong("amount",handler.getChemicalInTank(0).getAmount());
        }
        return infusion;
    }

    @Override
    public void load(CompoundNBT nbt) {
        capacity = nbt.getLong("capacity");
        handler.setStack(new InfusionStack(Objects.requireNonNull(MekanismAPI.infuseTypeRegistry().getValue(ResourceLocation.tryCreate(nbt.getString("infusion")))), nbt.getLong("amount")));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {

    }
}
