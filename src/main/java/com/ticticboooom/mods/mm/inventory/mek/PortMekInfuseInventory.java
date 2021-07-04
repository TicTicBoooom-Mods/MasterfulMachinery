package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;

public class PortMekInfuseInventory extends MekChemicalInventory<InfuseType, InfusionStack> implements IInfusionHandler {

    public PortMekInfuseInventory(long capacity) {
        super(InfusionStack.EMPTY, capacity, InfusionStack::new);
    }
}
