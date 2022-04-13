package com.ticticboooom.mods.mm.nbt.model;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NBTActionModel {
    private final String action;
    private final String key;
    private final JsonElement value;
}
