package com.ticticboooom.mods.mm.block.container;

import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class ControllerBlockContainer extends Container {

    @Getter
    private ControllerBlockEntity tile;

    public ControllerBlockContainer(@Nullable ContainerType<?> p_i50105_1_, int windowId, PlayerInventory inv, ControllerBlockEntity tile) {
        super(p_i50105_1_, windowId);
        this.tile = tile;
        int playerOffsetX = 8;
        int playerOffsetY = 141;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new Slot(inv, 9 + (j * 9 + i), i* 18 + playerOffsetX, j* 18 + playerOffsetY));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i,8 + (i * 18), 199));
        }
    }

    public ControllerBlockContainer(ContainerType<?> container, int windowId, PlayerInventory player, PacketBuffer buf) {
        this(container, windowId, player, (ControllerBlockEntity) player.player.level.getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }


}
