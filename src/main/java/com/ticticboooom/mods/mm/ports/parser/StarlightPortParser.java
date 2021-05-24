package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.AstralMachinePortBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.StarlightPortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import com.ticticboooom.mods.mm.registration.Registerable;
import lombok.SneakyThrows;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class StarlightPortParser extends PortFactory {

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<StarlightPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(StarlightPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }
    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(StarlightPortState.CODEC, ((StarlightPortState) state));
    }

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/starlight_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/starlight_cutout");
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<StarlightPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(StarlightPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(StarlightPortState.CODEC);
    }

    @Override
    public RegistryObject<MachinePortBlock> registerBlock(String id, DeferredRegister<Block> reg, Registerable<RegistryObject<TileEntityType<?>>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay) {
        return reg.register(id, () -> new AstralMachinePortBlock(type.get(), name, controllerId, textureOverride, overlay));
    }
}
