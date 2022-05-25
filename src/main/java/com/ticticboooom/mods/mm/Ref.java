package com.ticticboooom.mods.mm;

import com.electronwill.nightconfig.core.AbstractConfig;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

public class Ref {

    public static final String MOD_ID = "mm";

    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    public static ResourceLocation res(String path){
        return new ResourceLocation(MOD_ID, path);
    }

    public static final ResourceLocation CONTROLLER_OVERLAY_MODEL = res("controller_model");

    public static final class PortTypes {
        public static final ResourceLocation ITEM_TYPE = res("items");
        public static final ResourceLocation FLUID_TYPE = res("fluids");
        public static final ResourceLocation ENERGY_TYPE = res("energy");
    }

    public static final class Reg {
        public static final ResourceLocation STRUCTURE_KEY_TYPE = res("structures/key_type");
        public static final class SKT {
            public static final ResourceLocation BLOCK = res("block");
            public static final ResourceLocation PORT = res("port");
            public static final ResourceLocation PORT_TIER = res("port_tier");
            public static final ResourceLocation PORT_GROUP = res("port_group");
            public static final ResourceLocation MODIFIABLE = res("modifiable");
        }
        public static final ResourceLocation PROCESS_INGREDIENT_TYPE = res("processes/ingredient_type");
        public static final class PIT {
            public static final ResourceLocation ITEMS = res("items");
            public static final ResourceLocation ENERGY = res("energy");
        }

        public static final ResourceLocation PROCESS_OUTPUT_TYPE = res("processes/output_type");
        public static final class POT {
            public static final ResourceLocation ITEMS = res("items");
            public static final ResourceLocation ENERGY = res("energy");
        }

        public static final ResourceLocation PREPROCESSOR_TYPE = res("processes/preprocessor_type");
        public static final class PT {
            public static final ResourceLocation MODIFIABLE = res("modifiable");
        }

        public static final ResourceLocation MODIFIER_ACTIONS = res("processes/modifier_actions");
        public static final class MA {
            public static final ResourceLocation NOTHING = res("nothing");
            public static final ResourceLocation SPEED = res("speed");
        }
    }

    public static final ResourceLocation JEI_PLUGIN = res("jei");
}
