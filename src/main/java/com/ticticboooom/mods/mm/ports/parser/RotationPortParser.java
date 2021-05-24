package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.AstralMachinePortBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.RotationMachinePortBlock;
import com.ticticboooom.mods.mm.block.tile.RotationGenMachinePortBlockEntity;
import com.ticticboooom.mods.mm.block.tile.RotationMachinePortBlockEntity;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.RotationPortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.RotationPortStorage;
import com.ticticboooom.mods.mm.registration.Registerable;
import lombok.SneakyThrows;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class RotationPortParser extends PortFactory {

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<RotationPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(RotationPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(RotationPortState.CODEC, ((RotationPortState) state));
    }

    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
    }

    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/create_rotation_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/create_rotation_cutout");
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<RotationPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(RotationPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @Override
    @SneakyThrows
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(RotationPortState.CODEC);
    }

    @Override
    public RegistryObject<TileEntityType<?>> registerTileEntity(String id, DeferredRegister<TileEntityType<?>> reg, Registerable<RegistryObject<TileEntityType<?>>> tile, Registerable<RegistryObject<MachinePortBlock>> block, Registerable<RegistryObject<ContainerType<?>>> containerType, Supplier<PortStorage> portStorage, boolean isInput) {
        if (isInput) {
            return reg.register(id, () -> TileEntityType.Builder.create(() -> new RotationMachinePortBlockEntity(tile.get().get(), containerType.get().get(), portStorage.get(), isInput), block.get().get()).build(null));
        } else {
            return reg.register(id, () -> TileEntityType.Builder.create(() -> new RotationGenMachinePortBlockEntity(tile.get().get(), containerType.get().get(), portStorage.get(), isInput), block.get().get()).build(null));
        }
    }

    @Override
    public RegistryObject<MachinePortBlock> registerBlock(String id, DeferredRegister<Block> reg, Registerable<RegistryObject<TileEntityType<?>>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay) {
        return reg.register(id, () -> new RotationMachinePortBlock(type.get(), name, controllerId, textureOverride, overlay));
    }
}
