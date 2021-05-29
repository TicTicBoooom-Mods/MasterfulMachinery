package com.ticticboooom.mods.mm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProcessUpdate {
    private int ticksTaken;
    private String msg;
    private String id;
    private String sid;
    private List<Integer> takenIndices;
}
