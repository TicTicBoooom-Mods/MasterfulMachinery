package com.ticticboooom.mods.mm.network;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.network.packets.TileClientUpdatePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MM.ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int index = 0;
        INSTANCE.registerMessage(index++, TileClientUpdatePacket.Data.class, TileClientUpdatePacket.Data::encode, TileClientUpdatePacket.Data::decode, TileClientUpdatePacket::handle);
    }
}
