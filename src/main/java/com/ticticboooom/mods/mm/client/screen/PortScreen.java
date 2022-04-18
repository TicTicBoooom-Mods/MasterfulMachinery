package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import com.ticticboooom.mods.mm.client.container.PortContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PortScreen extends ContainerScreen<PortContainer> {
    public PortScreen(PortContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        super.render(stack, mouseX, mouseY, p_230430_4_);

        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x0, int y0) {
        drawString(stack, this.minecraft.fontRenderer, container.getTile().portModel.name, 7, -15, 0xfefefe);
        drawString(stack, this.minecraft.fontRenderer, "Inventory", 7, 110, 0xfefefe);

    }



    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        this.renderBackground(stack);
        container.getTile().storage.render(stack, x, y, this.guiLeft, this.guiTop, this);
    }
}
