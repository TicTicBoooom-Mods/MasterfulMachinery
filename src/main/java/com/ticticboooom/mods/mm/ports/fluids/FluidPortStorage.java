package com.ticticboooom.mods.mm.ports.fluids;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.util.FluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class FluidPortStorage extends PortStorage {

    private int amount;

    public MMFluidHandler handler;
    public LazyOptional<MMFluidHandler> handlerLO = LazyOptional.of(() -> handler);

    public FluidPortStorage(int amount) {
        this.amount = amount;
        handler = new MMFluidHandler(amount);
    }

    @Override
    public <T> LazyOptional<T> getLazyOptional(Capability<T> cap, Direction direction) {
        if (cap != CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return null;
        }
        return handlerLO.cast();
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT result = new CompoundNBT();
        result.putInt("capacity", amount);
        if (!handler.getFluidInTank(0).isEmpty()) {
            result.putString("fluid", handler.getFluidInTank(0).getFluid().getRegistryName().toString());
            result.putInt("storedFluid", handler.getFluidInTank(0).getAmount());
        }
        return result;
    }

    @Override
    public void load(CompoundNBT nbt) {
        amount = nbt.getInt("capacity");
        handler.setStack(new FluidStack(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryCreate(nbt.getString("fluid")))), nbt.getInt("storedFluid")));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Ref.MOD_ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top - 20, 0, 0, 175, 256);
        int x = 78;
        int y = 20;
        screen.blit(stack, left + x, top + y, 175, 0, 18, 18);
        FluidStack fluid = handler.getFluidInTank(0);
        if (!fluid.isEmpty()){
            FluidRenderer.INSTANCE.render(stack, left + x + 1, top + y + 1, handler.getFluidInTank(0), 16);

            AbstractGui.drawCenteredString(stack, Minecraft.getInstance().fontRenderer, handler.getFluidInTank(0).getAmount() + " " + handler.getFluidInTank(0).getDisplayName().getString(), left + x + 9 + 1, top + y + 30, 0xfefefe);
        }
    }
}
