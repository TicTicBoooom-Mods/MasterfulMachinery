package com.ticticboooom.mods.mm.client.jei.ingredients.model;

import lombok.Getter;
import lombok.Setter;

public class StarlightStack {
    @Getter
    @Setter
    private int amount;

    public StarlightStack(int amount){

        this.amount = amount;
    }
}
