package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.client.util.FluidRenderer;
import com.ticticboooom.mods.mm.inventory.PortFluidInventory;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidPortStorage implements IPortStorage {
    public static final Codec<FluidPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("capacity").forGetter(z -> z.inv.getTankCapacity(0))
    ).apply(x, FluidPortStorage::new));


    @Getter
    private final PortFluidInventory inv;
    private final LazyOptional<PortFluidInventory> invLO;

    public FluidPortStorage(int capacity) {
        this.inv = new PortFluidInventory(capacity);
        invLO =  LazyOptional.of(() -> this.inv);
    }

    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        inv.getFluidInTank(0).writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        inv.setStack(FluidStack.loadFluidStackFromNBT(nbt));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bind(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        int x = 78;
        int y = 40;
        screen.blit(stack, left + x, top + y, 175, 0, 18, 18);
        FluidRenderer.INSTANCE.render(stack, left + x + 1, top + y + 1, inv.getFluidInTank(0), 16);

        AbstractGui.drawCenteredString(stack, Minecraft.getInstance().font, inv.getFluidInTank(0).getAmount() + " " + inv.getFluidInTank(0).getDisplayName().getString(), left + x + 9 + 1, top + y + 30, 0xfefefe);
    }

    @Override
    public void setupContainer(PortBlockContainer container, PlayerInventory inv, MachinePortBlockEntity tile) {

    }
}
