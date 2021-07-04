package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;

public class PortMekGasInventory extends MekChemicalInventory<Gas, GasStack> implements IGasHandler {

    public PortMekGasInventory(long capacity) {
        super(GasStack.EMPTY, capacity, GasStack::new);
    }
}
