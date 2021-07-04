package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.ports.state.MekInfusePortState;
import com.ticticboooom.mods.mm.ports.state.MekSlurryPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.MekInfusePortStorage;
import com.ticticboooom.mods.mm.ports.storage.MekSlurryPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.SneakyThrows;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class MekInfusePortParser extends PortFactory {

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
        if (input) {
            ingredients.setInputs(MekanismJEI.TYPE_INFUSION, (List<InfusionStack>)stacks);
        } else {
            ingredients.setOutputs(MekanismJEI.TYPE_INFUSION, (List<InfusionStack>)stacks);
        }
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<MekInfusePortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(MekInfusePortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @SneakyThrows
    @Override
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(MekInfusePortState.CODEC);
    }

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<MekInfusePortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(MekInfusePortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(MekInfusePortState.CODEC, (MekInfusePortState)state);
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/mekanism_infusion_input_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/mekanism_infusion_input_cutout");
    }
}
