package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.keys.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MMRegistries {

    public static IForgeRegistry<StructureKeyType> STRUCTURE_KEY_TYPES;

    @SubscribeEvent
    public static void on(RegistryEvent.NewRegistry event) {
        STRUCTURE_KEY_TYPES = new RegistryBuilder<StructureKeyType>().setName(Ref.Reg.STRUCTURE_KEY_TYPE).setType(StructureKeyType.class).create();
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<StructureKeyType> event) {
        event.getRegistry().registerAll(
                new BlockStructureKeyType().setRegistryName(Ref.Reg.SKT.BLOCK),
                new PortStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT),
                new PortTierStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT_TIER),
                new PortGroupStructureKeyType().setRegistryName(Ref.Reg.SKT.PORT_GROUP),
                new ModifiableStructureKeyType().setRegistryName(Ref.Reg.SKT.MODIFIABLE)
        );
    }
}
