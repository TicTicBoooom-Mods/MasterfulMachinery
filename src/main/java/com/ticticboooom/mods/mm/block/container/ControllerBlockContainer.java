package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class ControllerBlockContainer extends Container {

    @Getter
    private ControllerBlockEntity tile;

    public ControllerBlockContainer(@Nullable ContainerType<?> p_i50105_1_, int windowId, ControllerBlockEntity tile) {
        super(p_i50105_1_, windowId);
        this.tile = tile;
    }

    public ControllerBlockContainer(ContainerType<?> container, int windowId, PlayerInventory player, PacketBuffer buf) {
        this(container, windowId, (ControllerBlockEntity) player.player.level.getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }


}
