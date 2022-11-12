package com.ticticboooom.mods.mm.client.jei.ingredients.model;

import lombok.Getter;
import lombok.Setter;

public class PressureStack {
    @Getter
    @Setter
    private float required;

    @Getter
    @Setter
    private float consumed;

    public PressureStack(float required, float consumed){
        this.required = required;
        this.consumed = consumed;
    }
}
