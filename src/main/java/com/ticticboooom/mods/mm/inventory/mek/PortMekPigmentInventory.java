package com.ticticboooom.mods.mm.inventory.mek;

import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;

public class PortMekPigmentInventory extends MekChemicalInventory<Pigment, PigmentStack> implements IPigmentHandler {

    public PortMekPigmentInventory(long capacity) {
        super(PigmentStack.EMPTY, capacity, PigmentStack::new);
    }
}
