package com.ticticboooom.mods.mm;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.system.CallbackI;

public class Ref {
    public static final String MOD_ID = "masterfulmachinery";
    public static ResourceLocation res(String path){
        return new ResourceLocation(MOD_ID, path);
    }

    public static final ResourceLocation CONTROLLER_OVERLAY_MODEL = res("controller_model");

    public static final ResourceLocation JEI_PLUGIN = res("jei");
}
