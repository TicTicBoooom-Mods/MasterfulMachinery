package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;

public class PortMekGasInventory extends MekChemicalInventory<Gas, GasStack> {

    public PortMekGasInventory(long capacity) {
        super(GasStack.EMPTY, capacity, GasStack::new);
    }
}
