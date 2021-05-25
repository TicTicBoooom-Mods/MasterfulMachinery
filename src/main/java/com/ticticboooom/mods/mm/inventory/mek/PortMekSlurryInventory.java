package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;

import java.util.function.BiFunction;

public class PortMekSlurryInventory extends MekChemicalInventory<Slurry, SlurryStack> {
    public PortMekSlurryInventory(long capacity) {
        super(SlurryStack.EMPTY, capacity, SlurryStack::new);
    }
}
