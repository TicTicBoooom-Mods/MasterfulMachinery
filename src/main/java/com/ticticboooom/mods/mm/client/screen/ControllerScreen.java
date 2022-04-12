package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ControllerScreen extends ContainerScreen<ControllerContainer> {
    public ControllerScreen(ControllerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    private static final ResourceLocation GUI = new ResourceLocation(Ref.MOD_ID, "textures/gui/gui_large.png");

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        super.render(stack, mouseX, mouseY, p_230430_4_);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x0, int y0) {
        this.minecraft.fontRenderer.func_238418_a_(container.getTile().controllerModel.name, 10, -20, 176, 0xfefefe);
        drawString(stack, this.minecraft.fontRenderer, "Inventory", 7, 100, 0xfefefe);
        int y = 40;
        for (String s : container.getTile().status.split("\n")) {
            drawString(stack, this.minecraft.fontRenderer, s, 12, y, 0xfefefe);
            y += 12;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        this.renderBackground(stack);
        this.minecraft.textureManager.bindTexture(GUI);
        this.blit(stack, this.guiLeft, this.guiTop - 30, 0, 0, 256, 256);
    }
}
