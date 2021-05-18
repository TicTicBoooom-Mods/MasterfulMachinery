package com.ticticboooom.mods.mm.ports.storage;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.KineticNetwork;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import lombok.Getter;
import lombok.Setter;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import me.desht.pneumaticcraft.common.util.DirectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RotationPortStorage extends PortStorage {

    @Getter
    @Setter
    private float speed;

    private HashMap<Direction, KineticTileEntity> kinetics = new HashMap<>();
    public RotationPortStorage() {
        neighborChanged();
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
    public void tick(MachinePortBlockEntity tile) {
        BlockPos blockPos = tile.getPos();
        World level = tile.getWorld();
        HashMap<Direction, TileEntity> tiles = new HashMap<>();
        tiles.put(Direction.EAST, level.getTileEntity(blockPos.add(1, 0, 0)));
        tiles.put(Direction.WEST, level.getTileEntity(blockPos.add(-1, 0, 0)));
        tiles.put(Direction.UP, level.getTileEntity(blockPos.add(0, 1, 0)));
        tiles.put(Direction.DOWN, level.getTileEntity(blockPos.add(0, -1, 0)));
        tiles.put(Direction.NORTH, level.getTileEntity(blockPos.add(0, 0, 1)));
        tiles.put(Direction.SOUTH, level.getTileEntity(blockPos.add(0, 0, -1)));
        speed = 0;
        if (tile.isInput()) {
            for (Map.Entry<Direction, TileEntity> tileEntity : tiles.entrySet()) {
                    if (tileEntity.getValue() instanceof KineticTileEntity) {
                    KineticTileEntity te = (KineticTileEntity) tileEntity.getValue();
                    if (Math.abs(te.getSpeed()) > speed){
                        speed = Math.abs(te.getSpeed());
                    }
                }
            }
        } else {
            for (Map.Entry<Direction, TileEntity> tileEntity : tiles.entrySet()) {
                if (tileEntity.getValue() instanceof KineticTileEntity) {

                        KineticTileEntity te = (KineticTileEntity) tileEntity.getValue();
                    if (speed == 0){
                        te.detachKinetics();
                        te.setSpeed(0);
                        te.setNetwork(null);
                    } else {
                        te.setSpeed(speed);
                        te.setNetwork(te.getPos().toLong());
                        te.attachKinetics();
                    }
                        te.notifyUpdate();
                }
            }
        }
    }
}
