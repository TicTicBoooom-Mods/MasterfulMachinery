package com.ticticboooom.mods.mm.cap;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.setup.MMItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Capabilities {

    @CapabilityInject(IBlueprintData.class)
    public static Capability<IBlueprintData> BLUEPRINT_DATA = null;

    @SubscribeEvent
    public static void on(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == MMItems.BLUEPRINT.get()) {
            ResourceLocation structure_capability = new ResourceLocation(Ref.MOD_ID, "structure_capability");
            event.addCapability(structure_capability, new BlueprintDataProvider());
        }
    }
}
