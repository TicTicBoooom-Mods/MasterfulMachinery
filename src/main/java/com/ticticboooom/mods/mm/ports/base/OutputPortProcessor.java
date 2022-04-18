package com.ticticboooom.mods.mm.ports.base;

import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;

public abstract class OutputPortProcessor extends PortProcessor {
    public abstract boolean process(MachineProcessContext ctx);
}
