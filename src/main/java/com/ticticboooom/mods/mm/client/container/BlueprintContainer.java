package com.ticticboooom.mods.mm.client.container;

import com.ticticboooom.mods.mm.client.container.slot.BlueprintSlot;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class BlueprintContainer extends Container {

    public IntReferenceHolder indexHolder = IntReferenceHolder.single();

    public BlueprintContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(MMContainerTypes.BLUEPRINT.get(), windowId);
        int index = 0;
        for (int i = 0; i < 11; i++) {
            addSlot(new BlueprintSlot(index, -35 + 1, -15 + (i * 18)+ 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + 18+ 1, -15 + (i * 18)+ 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + (2 * 18)+ 1, -15 + (i * 18)+ 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + (3 * 18)+ 1, -15 + (i * 18)+ 1));
            index++;
        }
    }

    public StructureModel structure;

    public void rotateDisplayedStructureForward() {
        indexHolder.set(indexHolder.get() + 1);
        if (indexHolder.get() >= DataRegistry.STRUCTURES.size()) {
            indexHolder.set(0);
        }
        this.structure = DataRegistry.STRUCTURES.values().toArray(new StructureModel[0])[indexHolder.get()];
    }

    public void rotateDisplayedStructureBackward() {
        indexHolder.set(indexHolder.get() - 1);
        if (indexHolder.get() < 0) {
            indexHolder.set(DataRegistry.STRUCTURES.size() - 1);
        }
        this.structure = DataRegistry.STRUCTURES.values().toArray(new StructureModel[0])[indexHolder.get()];
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

}
