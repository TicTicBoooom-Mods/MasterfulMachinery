package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.ports.state.EnergyPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.SneakyThrows;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

public class EnergyPortParser implements IPortParser {

    @Override
    public Supplier<IPortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<EnergyPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(EnergyPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.writeWithCodec(EnergyPortState.CODEC, ((EnergyPortState) state));
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<EnergyPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(EnergyPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.readWithCodec(EnergyPortState.CODEC);
    }
}
