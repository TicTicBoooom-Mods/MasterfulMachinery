package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.ports.state.FluidPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.FluidPortStorage;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.SneakyThrows;
import net.minecraft.network.PacketBuffer;

import java.util.function.Supplier;

public class FluidPortParser implements IPortParser {

    @Override
    public Supplier<IPortStorage> createStorage(JsonObject obj) {
        return () -> {
                DataResult<Pair<FluidPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(FluidPortStorage.CODEC).apply(obj);
                return apply.result().get().getFirst();
            };
        }
    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.writeWithCodec(FluidPortState.CODEC, ((FluidPortState) state));
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<FluidPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(FluidPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.readWithCodec(FluidPortState.CODEC);
    }
}
