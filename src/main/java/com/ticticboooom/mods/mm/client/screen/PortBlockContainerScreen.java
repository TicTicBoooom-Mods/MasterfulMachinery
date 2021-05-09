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
    protected void renderBg(MatrixStack stack, float p_230450_2_, int mouseX, int mouseY) {
        this.renderBackground(stack);
        container.getTile().getStorage().render(stack, mouseX, mouseY, this.leftPos, this.topPos, this);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int p_230451_2_, int p_230451_3_) {
        drawString(stack, this.minecraft.font, container.getTile().getDisplayName(), 7, 5, 0xfefefe);
        drawString(stack, this.minecraft.font, "Inventory", 7, 130, 0xfefefe);
    }
}
