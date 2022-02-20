package com.ticticboooom.mods.mm;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.system.CallbackI;

public class Ref {
    public static final String MOD_ID = "mm";
    public static ResourceLocation res(String path){
        return new ResourceLocation(MOD_ID, path);
    }

    public static final ResourceLocation CONTROLLER_OVERLAY_MODEL = res("controller_model");

    public static final class PortTypes {
        public static final ResourceLocation ITEM_TYPE = res("items");
    }

    public static final class Reg {
        public static final ResourceLocation STRUCTURE_KEY_TYPE = res("structures/key_type");
        public static final class SKT {
            public static final ResourceLocation BLOCK = res("block");
            public static final ResourceLocation PORT = res("port");
            public static final ResourceLocation PORT_TIER = res("port_tier");
        }
    }

    public static final ResourceLocation JEI_PLUGIN = res("jei");
}
