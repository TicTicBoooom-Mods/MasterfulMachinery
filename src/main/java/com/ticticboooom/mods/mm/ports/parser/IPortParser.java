package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.state.IPortState;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

public interface IPortParser {
    IPortState createState(JsonObject obj);
    IPortState createState(PacketBuffer buf);
    Supplier<IPortStorage> createStorage(JsonObject obj);
    void write(PacketBuffer buf, IPortState state);
}
