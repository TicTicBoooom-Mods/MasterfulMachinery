package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.tile.IMachinePortTile;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;

public class RotationPortStorage extends PortStorage {
    public static final Codec<RotationPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("stress").forGetter(z -> z.stress)
    ).apply(x, RotationPortStorage::new));

    @Getter
    @Setter
    private float speed;

    @Getter
    private boolean isOverStressed;

    @Getter
    private int stress;

    public RotationPortStorage(int stress) {
        this.stress = stress;
    }

    private HashMap<Direction, KineticTileEntity> kinetics = new HashMap<>();
    public RotationPortStorage() {
    }

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
        nbt.putFloat("speed", speed);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        speed = nbt.getFloat("speed");
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        AbstractGui.drawCenteredString(stack, Minecraft.getInstance().fontRenderer, speed + " Speed", left + 60, top + 30, 0xfefefe);
    }

    @Override
    public void tick(IMachinePortTile tile) {
        KineticTileEntity kinetic = tile.getTile();
        this.isOverStressed = kinetic.isOverStressed();
        //this.speed = Math.abs(kinetic.getSpeed());
    }

    @Override
    public void onRecipeInterrupted(MachineProcessRecipe recipe) {
        this.speed = 0;
    }
}
