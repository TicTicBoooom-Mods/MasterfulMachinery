package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;

public class PortMekSlurryInventory extends MekChemicalInventory<Slurry, SlurryStack> implements ISlurryHandler, ISlurryTank {
    public PortMekSlurryInventory(long capacity) {
        super(SlurryStack.EMPTY, capacity, SlurryStack::new);
    }
}
