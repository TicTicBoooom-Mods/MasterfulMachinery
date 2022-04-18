package com.ticticboooom.mods.mm.process;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ModifierAction extends ForgeRegistryEntry<ModifierAction> {

    public abstract Value parse(JsonObject json);
    public abstract boolean modify(Value val, MachineProcessContext ctx);

    public static class Value {
        public ResourceLocation type;
    }
}
