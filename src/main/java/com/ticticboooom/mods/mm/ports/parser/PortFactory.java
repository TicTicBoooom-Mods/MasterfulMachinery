package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.datagen.gen.runtime.MMBlockStateProvider;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.model.ModelOverrideModel;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.registration.MMSetup;
import com.ticticboooom.mods.mm.registration.Registerable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public abstract class PortFactory {
    public abstract PortState createState(JsonObject obj);

    public abstract PortState createState(PacketBuffer buf);

    public abstract Supplier<PortStorage> createStorage(JsonObject obj);

    public abstract void write(PacketBuffer buf, PortState state);

    public abstract void setIngredients(IIngredients ingredients, List<?> stacks, boolean input);

    public abstract ResourceLocation getInputOverlay();

    public abstract ResourceLocation getOutputOverlay();

    public RegistryObject<TileEntityType<?>> registerTileEntity(String id, DeferredRegister<TileEntityType<?>> reg, Registerable<RegistryObject<TileEntityType<?>>> tile, Registerable<RegistryObject<MachinePortBlock>> block, Registerable<RegistryObject<ContainerType<?>>> containerType, Supplier<PortStorage> portStorage, boolean isInput) {
        return reg.register(id, () -> TileEntityType.Builder.create(() -> new MachinePortBlockEntity(tile.get().get(), containerType.get().get(), portStorage.get(), isInput), block.get().get()).build(null));
    }

    public RegistryObject<MachinePortBlock> registerBlock(String id, DeferredRegister<Block> reg, Registerable<RegistryObject<TileEntityType<?>>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay, ResourceLocation portTypeId) {
        return reg.register(id, () -> new MachinePortBlock(type.get(), name, controllerId, textureOverride, overlay, portTypeId));
    }


    public void generateBlockStates(MMBlockStateProvider provider, boolean input, RegistryObject<MachinePortBlock> port) {
        if (input) {
            provider.dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : MMBlockStateProvider.BASE_TEXTURE, port.get().getOverlay());
            provider.simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        } else {
            provider.dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : MMBlockStateProvider.BASE_TEXTURE, port.get().getOverlay());
            provider.simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }
    }
}
