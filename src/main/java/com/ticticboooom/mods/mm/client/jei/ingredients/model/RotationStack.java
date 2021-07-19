package com.ticticboooom.mods.mm.client.jei.ingredients.model;

import lombok.Getter;
import lombok.Setter;

public class RotationStack {
    @Getter
    @Setter
    private float speed;

    public RotationStack(float speed){

        this.speed = speed;
    }
}
