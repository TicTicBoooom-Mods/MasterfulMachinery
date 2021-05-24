package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.nbt.NBTActionParser;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.state.ItemPortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.ItemPortStorage;
import lombok.SneakyThrows;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class ItemPortParser extends PortFactory {


    @Override
    public void setIngredients(IIngredients ingredients, List<?> stacks, boolean input) {
        if (input) {
            ingredients.setInputs(VanillaTypes.ITEM, (List<ItemStack>)stacks);
        } else {
            ingredients.setOutputs(VanillaTypes.ITEM, (List<ItemStack>)stacks);
        }
    }

    @Override
    public Supplier<PortStorage> createStorage(JsonObject obj) {
        return () -> {
            DataResult<Pair<ItemPortStorage, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ItemPortStorage.CODEC).apply(obj);
            return apply.result().get().getFirst();
        };
    }

    @SneakyThrows
    @Override
    public void write(PacketBuffer buf, PortState state) {
        buf.func_240629_a_(ItemPortState.CODEC, ((ItemPortState) state));
        NBTActionParser.write(buf, ((ItemPortState) state).getNbt());
    }

    @Override
    public PortState createState(JsonObject obj) {
        DataResult<Pair<ItemPortState, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(ItemPortState.CODEC).apply(obj);
        ItemPortState result = apply.result().get().getFirst();
        if (obj.has("nbt")){
            JsonElement nbt = obj.get("nbt");
            NBTModel parse = NBTActionParser.parse(nbt.getAsJsonArray());
            result.setNbt(parse);
        }
        return result;

    }

    @SneakyThrows
    @Override
    public PortState createState(PacketBuffer buf) {
        ItemPortState state = buf.func_240628_a_(ItemPortState.CODEC);
        NBTModel parsed = NBTActionParser.parse(buf);
        state.setNbt(parsed);
        return state;

    }
    @Override
    public ResourceLocation getInputOverlay() {
        return new ResourceLocation(MM.ID, "block/base_ports/item_input_cutout");
    }

    @Override
    public ResourceLocation getOutputOverlay() {
        return new ResourceLocation(MM.ID, "block/base_ports/item_output_cutout");
    }
}
