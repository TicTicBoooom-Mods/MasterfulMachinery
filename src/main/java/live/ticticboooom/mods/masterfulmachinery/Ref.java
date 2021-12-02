package live.ticticboooom.mods.masterfulmachinery;

import net.minecraft.resources.ResourceLocation;

public class Ref {
    public static final String MOD_ID = "masterfulmachinery";
    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static final class Ports {
        public static final ResourceLocation ITEMS = res("items");
    }
}
