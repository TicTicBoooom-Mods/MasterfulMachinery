package com.ticticboooom.mods.mm.process;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ProcessOutputType extends ForgeRegistryEntry<ProcessOutputType> {
    public abstract Value parse(JsonObject object);
    public abstract boolean canProcess(Value val, MachineProcessContext ctx);
    public abstract void process(Value val, MachineProcessContext ctx);

    public abstract static class Value {
        public Value(ResourceLocation type) {
            this.type = type;
        }
        public ResourceLocation type;
    }
}
