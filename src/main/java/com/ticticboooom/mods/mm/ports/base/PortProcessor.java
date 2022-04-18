package com.ticticboooom.mods.mm.ports.base;

import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;

public abstract class PortProcessor {
    public abstract boolean canProcess(MachineProcessContext ctx);
}
