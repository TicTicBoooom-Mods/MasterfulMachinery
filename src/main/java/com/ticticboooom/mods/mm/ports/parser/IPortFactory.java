package com.ticticboooom.mods.mm.ports.parser;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public interface IPortFactory {
    PortState createState(JsonObject obj);
    PortState createState(PacketBuffer buf);
    Supplier<PortStorage> createStorage(JsonObject obj);
    void write(PacketBuffer buf, PortState state);
    void setIngredients(IIngredients ingredients, List<?> stacks, boolean input);
    ResourceLocation getInputOverlay();
    ResourceLocation getOutputOverlay();
}
