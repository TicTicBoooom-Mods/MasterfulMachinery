package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

public interface IPortParser {
    PortState createState(JsonObject obj);
    PortState createState(PacketBuffer buf);
    Supplier<IPortStorage> createStorage(JsonObject obj);
    void write(PacketBuffer buf, PortState state);
}
