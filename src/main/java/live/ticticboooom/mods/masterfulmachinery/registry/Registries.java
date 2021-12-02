package live.ticticboooom.mods.masterfulmachinery.registry;

import live.ticticboooom.mods.masterfulmachinery.Ref;
import live.ticticboooom.mods.masterfulmachinery.ports.base.MasterfulPortType;
import live.ticticboooom.mods.masterfulmachinery.ports.types.ItemsPortType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class Registries {

    public static Map<ResourceLocation, MasterfulPortType> PORTS = new HashMap<>();

    public static void registerPort() {
        PORTS.put(Ref.Ports.ITEMS, new ItemsPortType());
    }
}
