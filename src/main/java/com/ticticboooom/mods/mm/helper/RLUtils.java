package com.ticticboooom.mods.mm.helper;

import net.minecraft.util.ResourceLocation;

public class RLUtils {
    public static ResourceLocation toRL(String str) {
        return ResourceLocation.tryParse(str);
    }

    public static boolean isRL(String str) {
        return ResourceLocation.tryParse(str) != null;
    }
}
