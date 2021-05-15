package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ControllerBlockContainerScreen extends ContainerScreen<ControllerBlockContainer> {
    private final ControllerBlockContainer container;

    public ControllerBlockContainerScreen(ControllerBlockContainer container, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(container, p_i51105_2_, p_i51105_3_);
        this.container = container;
    }

    private static final ResourceLocation GUI = new ResourceLocation(MM.ID, "textures/gui/gui_large.png");

    @Override
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.renderBackground(stack);
        this.minecraft.textureManager.bind(GUI);
        this.blit(stack, this.leftPos, this.topPos - 20, 0, 0, 256, 256);
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int p_230451_2_, int p_230451_3_) {
        this.minecraft.font.drawWordWrap(container.getTile().getDisplayName(), 10, -10,   176, 0xfefefe);
        drawString(stack, this.minecraft.font, "Inventory", 7, 110, 0xfefefe);
        int y = 50;
        for (String s : container.getTile().getUpdate().getMsg().split("\n")) {
            drawString(stack, this.minecraft.font, s, 12, y, 0xfefefe);
            y += 12;
        }

    }
}
