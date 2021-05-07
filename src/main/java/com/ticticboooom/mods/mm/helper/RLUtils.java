package com.ticticboooom.mods.mm.helper;

import net.minecraft.util.ResourceLocation;

public class RLUtils {
    public static ResourceLocation toRL(String str) {
        String[] split = str.split(":");
        return new ResourceLocation(split[0], split[1]);
    }
}
