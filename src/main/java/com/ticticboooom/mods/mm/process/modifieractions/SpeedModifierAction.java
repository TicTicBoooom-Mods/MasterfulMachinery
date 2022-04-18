package com.ticticboooom.mods.mm.process.modifieractions;

import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.process.ModifierAction;

public class SpeedModifierAction extends ModifierAction {
    @Override
    public Value parse(JsonObject json) {
        SpeedValue speedValue = new SpeedValue();
        speedValue.percentageDifference = json.get("ticks").getAsDouble();
        return speedValue;
    }

    @Override
    public boolean modify(Value val, MachineProcessContext ctx) {
        SpeedValue speed = (SpeedValue) val;
        ctx.ticks *= speed.percentageDifference;
        return false;
    }

    public static final class SpeedValue extends Value {
        public Double percentageDifference;
    }
}
