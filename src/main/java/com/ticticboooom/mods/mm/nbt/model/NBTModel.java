package com.ticticboooom.mods.mm.nbt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NBTModel {
    private final List<NBTActionModel> actions;
}
