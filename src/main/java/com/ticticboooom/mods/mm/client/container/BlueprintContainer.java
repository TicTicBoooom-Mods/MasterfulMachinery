package com.ticticboooom.mods.mm.client.container;

import com.ticticboooom.mods.mm.client.container.slot.BlueprintSlot;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class BlueprintContainer extends Container {
    public BlueprintContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(MMContainerTypes.BLUEPRINT.get(), windowId);
        int index = 0;
        for (int i = 0; i < 9; i++) {
            addSlot(new BlueprintSlot(index, 40+ (i * 18), 110));
            index++;
        }
    }

    public StructureModel structure;


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

}
