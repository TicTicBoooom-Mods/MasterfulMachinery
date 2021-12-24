package com.ticticboooom.mods.mm.ports.base;

public abstract class PortProcessor {
    public abstract boolean canProcess(MachineRecipeContext ctx, PortStorage current);
    public abstract boolean process(MachineRecipeContext ctx, PortStorage current);
}
