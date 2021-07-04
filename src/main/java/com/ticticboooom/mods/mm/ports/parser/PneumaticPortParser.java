package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.MMJeiPlugin;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.ports.state.PneumaticPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.PneumaticPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.SneakyThrows;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class PneumaticPortParser extends PortFactory {

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<PneumaticPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(PneumaticPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }
    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(PneumaticPortState.CODEC, ((PneumaticPortState) state));
    }

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
        if (input) {
            ingredients.setInputs(MMJeiPlugin.PRESSURE_TYPE, (List<PressureStack>)stacks);
        } else {
            ingredients.setOutputs(MMJeiPlugin.PRESSURE_TYPE, (List<PressureStack>)stacks);
        }
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/pncr_pressure_input_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/pncr_pressure_output_cutout");
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<PneumaticPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(PneumaticPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(PneumaticPortState.CODEC);
    }
}
