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
    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        super.render(stack, mouseX, mouseY, p_230430_4_);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x0, int y0) {
        this.minecraft.fontRenderer.func_238418_a_(container.getTile().getDisplayName(), 10, -10, 176, 0xfefefe);
        drawString(stack, this.minecraft.fontRenderer, "Inventory", 7, 110, 0xfefefe);
        int y = 50;
        for (String s : container.getTile().getUpdate().getMsg().split("\n")) {
            drawString(stack, this.minecraft.fontRenderer, s, 12, y, 0xfefefe);
            y += 12;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        this.renderBackground(stack);
        this.minecraft.textureManager.bindTexture(GUI);
        this.blit(stack, this.guiLeft, this.guiTop - 20, 0, 0, 256, 256);
    }
}
