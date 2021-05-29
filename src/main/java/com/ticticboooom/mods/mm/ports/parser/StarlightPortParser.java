package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.AstralMachinePortBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.tile.AstralMachinePortBlockEntity;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.StarlightPortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
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
        return new ResourceLocation(MM.ID, "block/compat_ports/astral_starlight_input_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/compat_ports/astral_starlight_output_cutout");
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

    @Override
    public RegistryObject<TileEntityType<?>> registerTileEntity(String id, DeferredRegister<TileEntityType<?>> reg, Registerable<RegistryObject<TileEntityType<?>>> tile, Registerable<RegistryObject<MachinePortBlock>> block, Registerable<RegistryObject<ContainerType<?>>> containerType, Supplier<PortStorage> portStorage, boolean isInput) {

        if (isInput){
            return super.registerTileEntity(id, reg, tile, block, containerType, portStorage, isInput);
        } else {
            return reg.register(id, () -> TileEntityType.Builder.create(() -> new AstralMachinePortBlockEntity(tile.get().get(), containerType.get().get(), portStorage.get()), block.get().get()).build(null));
        }

    }
}
