package com.ticticboooom.mods.mm.client.jei.ingredients.model;

import lombok.Getter;
import lombok.Setter;

public class PressureStack {
    @Getter
    @Setter
    private float amount;

    public PressureStack(float amount){

        this.amount = amount;
    }
}
