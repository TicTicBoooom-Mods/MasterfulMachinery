package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.tile.ManaMachinePortBlockEntity;
import com.ticticboooom.mods.mm.ports.state.ManaPortState;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.ManaPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.registration.MMSetup;
import com.ticticboooom.mods.mm.registration.Registerable;
import lombok.SneakyThrows;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ManaPortParser extends PortFactory {

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<ManaPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ManaPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(ManaPortState.CODEC, ((ManaPortState) state));
    }

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/botania_mana_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/botania_mana_cutout");
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<ManaPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ManaPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(ManaPortState.CODEC);
    }

    @Override
    public RegistryObject<TileEntityType<?>> registerTileEntity(String id, DeferredRegister<TileEntityType<?>> reg, Registerable<RegistryObject<TileEntityType<?>>>tile, Registerable<RegistryObject<MachinePortBlock>> block, Registerable<RegistryObject<ContainerType<?>>> containerType, Supplier<PortStorage> portStorage, boolean isInput){
        return reg.register(id, () -> TileEntityType.Builder.create(() -> new ManaMachinePortBlockEntity(tile.get().get(), containerType.get().get(), portStorage.get(), true), block.get().get()).build(null));
    }
}
