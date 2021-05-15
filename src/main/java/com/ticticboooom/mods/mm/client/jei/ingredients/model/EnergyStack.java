package com.ticticboooom.mods.mm.client.jei.ingredients.model;

import lombok.Getter;
import lombok.Setter;

public class EnergyStack {
    @Getter
    @Setter
    private int amount;

    public EnergyStack(int amount){

        this.amount = amount;
    }
}
