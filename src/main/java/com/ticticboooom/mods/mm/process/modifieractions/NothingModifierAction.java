package com.ticticboooom.mods.mm.process.modifieractions;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.process.ModifierAction;

public class NothingModifierAction extends ModifierAction {
    @Override
    public Value parse(JsonObject json) {
        return new NothingValue();
    }

    @Override
    public boolean modify(Value val, MachineProcessContext ctx) {
        return true;
    }

    public static final class NothingValue extends Value {

    }
}
