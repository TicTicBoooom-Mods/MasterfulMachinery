package com.ticticboooom.mods.mm.net;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.net.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.net.packets.UpdateBluprintItemPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MMNetworkManager {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Ref.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(i++, TileClientUpdatePacket.Data.class, TileClientUpdatePacket.Data::encode, TileClientUpdatePacket.Data::decode, TileClientUpdatePacket::handle);
        INSTANCE.registerMessage(i++, UpdateBluprintItemPacket.Data.class, UpdateBluprintItemPacket.Data::encode, UpdateBluprintItemPacket.Data::decode, UpdateBluprintItemPacket::handle);
    }
}
