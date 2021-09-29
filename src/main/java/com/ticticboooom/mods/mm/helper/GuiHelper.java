package com.ticticboooom.mods.mm.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;

public class GuiHelper {

    public static void renderVerticallyFilledBar(MatrixStack ms, Screen screen, int x, int y, int u, int v, int width, int height, float pct) {
        float invPct = 1 - pct;
        screen.blit(ms, x, y + (int) (height * invPct), u, v + (int) (height * invPct), width, (int) (height * pct));
    }

}
