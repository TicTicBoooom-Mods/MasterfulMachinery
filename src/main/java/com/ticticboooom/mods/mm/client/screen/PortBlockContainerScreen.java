package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PortBlockContainerScreen extends ContainerScreen<PortBlockContainer> {
    private final PortBlockContainer container;

    public PortBlockContainerScreen(PortBlockContainer container, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(container, p_i51105_2_, p_i51105_3_);
        this.container = container;
    }

    private static final ResourceLocation GUI = new ResourceLocation(MM.ID, "textures/gui/port_gui.png");
    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderHoveredTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        this.renderBackground(stack);
        container.getTile().getStorage().render(stack, x, y, this.guiLeft, this.guiTop, this);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x, int y) {
        drawString(stack, this.minecraft.fontRenderer, container.getTile().getDisplayName(), 7, 5, 0xfefefe);
        drawString(stack, this.minecraft.fontRenderer, "Inventory", 7, 130, 0xfefefe);
    }

}
