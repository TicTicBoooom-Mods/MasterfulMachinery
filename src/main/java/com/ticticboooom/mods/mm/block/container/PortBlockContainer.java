package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class PortBlockContainer extends Container {

    private MachinePortBlockEntity tile;

    protected PortBlockContainer(@Nullable ContainerType<?> p_i50105_1_, int windowId, MachinePortBlockEntity tile) {
        super(p_i50105_1_, windowId);
        this.tile = tile;
    }

    public PortBlockContainer(ContainerType<?> container, int windowId, PlayerInventory player, PacketBuffer buf) {
        this(container, windowId, (MachinePortBlockEntity) player.player.level.getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }


}
