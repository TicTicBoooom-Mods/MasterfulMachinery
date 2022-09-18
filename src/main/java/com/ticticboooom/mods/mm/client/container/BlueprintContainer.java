package com.ticticboooom.mods.mm.client.container;

import com.ticticboooom.mods.mm.cap.Capabilities;
import com.ticticboooom.mods.mm.cap.IBlueprintData;
import com.ticticboooom.mods.mm.client.container.slot.BlueprintSlot;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.net.packets.UpdateBluprintItemPacket;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlueprintContainer extends Container {

    public IntReferenceHolder indexHolder = IntReferenceHolder.single();
    private PlayerInventory inv;

    public BlueprintContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(MMContainerTypes.BLUEPRINT.get(), windowId);
        ItemStack currentItem = inv.player.getHeldItemMainhand();
        Optional<IBlueprintData> bp = currentItem.getCapability(Capabilities.BLUEPRINT_DATA).resolve();
        if (bp.isPresent()) {
            ResourceLocation structureId = bp.get().getStructure();
            if (structureId != null) {
                structure = DataRegistry.STRUCTURES.get(structureId);
            }
        }

        this.inv = inv;
        int index = 0;
        for (int i = 0; i < 11; i++) {
            addSlot(new BlueprintSlot(index, -35 + 1, -15 + (i * 18) + 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + 18 + 1, -15 + (i * 18) + 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + (2 * 18) + 1, -15 + (i * 18) + 1));
            index++;
            addSlot(new BlueprintSlot(index, -35 + (3 * 18) + 1, -15 + (i * 18) + 1));
            index++;
        }

        controllerSlot = (BlueprintSlot) addSlot(new BlueprintSlot(index++, 185, 116));
    }

    public StructureModel structure;
    public BlueprintSlot controllerSlot;

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

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        ItemStack item = playerIn.getHeldItemMainhand();
        IBlueprintData data = item.getCapability(Capabilities.BLUEPRINT_DATA).resolve().get();
        if (structure != null) {
            data.setStructure(this.structure.id);
                MMNetworkManager.INSTANCE.sendToServer(new UpdateBluprintItemPacket.Data(playerIn.getUniqueID(), this.structure.id));
        }
        super.onContainerClosed(playerIn);
    }
}
