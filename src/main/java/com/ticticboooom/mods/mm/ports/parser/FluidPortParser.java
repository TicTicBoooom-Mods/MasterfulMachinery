package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.ports.state.FluidPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.FluidPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.SneakyThrows;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Supplier;

public class FluidPortParser extends PortFactory {

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
                DataResult<Pair<FluidPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(FluidPortStorage.CODEC).apply(obj);
                return apply.result().get().getFirst();
            };
        }
    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(FluidPortState.CODEC, ((FluidPortState) state));
    }

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
        if (input) {
            ingredients.setInputs(VanillaTypes.FLUID, (List<FluidStack>)stacks);
        } else {
            ingredients.setOutputs(VanillaTypes.FLUID, (List<FluidStack>)stacks);
        }
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<FluidPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(FluidPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(FluidPortState.CODEC);
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/base_ports/fluid_input_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/base_ports/fluid_output_cutout");
    }
}
