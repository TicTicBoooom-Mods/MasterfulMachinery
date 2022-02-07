package com.ticticboooom.mods.mm.ports.items;

import com.ticticboooom.mods.mm.ports.base.MachineRecipeContext;
import com.ticticboooom.mods.mm.ports.base.PortProcessor;
import com.ticticboooom.mods.mm.ports.base.PortStorage;

public class ItemPortInputProcessor extends PortProcessor {
    @Override
    public boolean canProcess(MachineRecipeContext ctx, PortStorage current) {
        return false;
    }

    @Override
    public boolean process(MachineRecipeContext ctx, PortStorage current) {
        return false;
    }
}
