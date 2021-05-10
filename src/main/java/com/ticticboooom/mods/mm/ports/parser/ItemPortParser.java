package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.ItemPortState;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.ports.storage.ItemPortStorage;
import lombok.SneakyThrows;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

public class ItemPortParser implements IPortParser {

    @Override
    public Supplier<IPortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<ItemPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ItemPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.writeWithCodec(ItemPortState.CODEC, ((ItemPortState) state));
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<ItemPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ItemPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @SneakyThrows
    @Override
    public PortState createState(PacketBuffer buf) {
        return buf.readWithCodec(ItemPortState.CODEC);
    }
}
