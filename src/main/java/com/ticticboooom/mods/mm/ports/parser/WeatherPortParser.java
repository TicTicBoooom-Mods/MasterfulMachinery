package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.StarlightPortState;
import com.ticticboooom.mods.mm.ports.state.WeatherPortState;
import com.ticticboooom.mods.mm.ports.storage.EnergyPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.WeatherPortStorage;
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

public class WeatherPortParser extends PortFactory {
    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<WeatherPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(WeatherPortState.CODEC).apply(obj);
        return apply.result().get().getFirst();
    }

    @SneakyThrows
    @Override
    public PortState createState(PacketBuffer buf) {
        return buf.func_240628_a_(StarlightPortState.CODEC);
    }

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return WeatherPortStorage::new;
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(WeatherPortState.CODEC, ((WeatherPortState) state));
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
    public RegistryObject<MachinePortBlock> registerBlock(String id, DeferredRegister<Block> reg, Registerable<RegistryObject<TileEntityType<?>>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay, ResourceLocation portTypeId) {
        return super.registerBlock(id, reg, type, name, controllerId, textureOverride, overlay, portTypeId);
    }

    @Override
    public RegistryObject<TileEntityType<?>> registerTileEntity(String id, DeferredRegister<TileEntityType<?>> reg, Registerable<RegistryObject<TileEntityType<?>>> tile, Registerable<RegistryObject<MachinePortBlock>> block, Registerable<RegistryObject<ContainerType<?>>> containerType, Supplier<PortStorage> portStorage, boolean isInput) {
            return super.registerTileEntity(id, reg, tile, block, containerType, portStorage, isInput);
    }
}
