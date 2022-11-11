package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.model.ControllerDisplayData;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;

public class ControllerBlockContainerScreen extends ContainerScreen<ControllerBlockContainer> {
    private final ControllerBlockContainer container;

    public ControllerBlockContainerScreen(ControllerBlockContainer container, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(container, p_i51105_2_, p_i51105_3_);
        this.container = container;
    }

    private static final ResourceLocation GUI = new ResourceLocation(MM.ID, "textures/gui/gui_large.png");

    @Override
    protected void init() {
        this.xSize = 174;
        this.ySize = 222;
        super.init();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        super.render(stack, mouseX, mouseY, p_230430_4_);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int x0, int y0) {
        //this.minecraft.fontRenderer.func_238418_a_(container.getTile().getDisplayName(), 10, -10, 176, 0xfefefe);
        ControllerDisplayData data = container.getTile().getDisplayData();
        // Structure Name
        String structureName = "Unknown Structure";
        if (!StringUtils.isNullOrEmpty(data.getStructureName())) {
            structureName = data.getStructureName();
        }
        drawCenteredString(stack, this.minecraft.fontRenderer, structureName, this.xSize / 2, 10, 0xfefefe);

        int baseInfoY = 56;
        int infoOffset = 0;
        // Recipe Name
        if (!StringUtils.isNullOrEmpty(data.getRecipeName())) {
            String recipeName = data.getRecipeName();
            drawString(stack, this.font, "Recipe: " + recipeName, 10, baseInfoY + (this.font.FONT_HEIGHT + 2) * infoOffset, 0xfefefe);
            infoOffset++;
        }

        // Status
        String status = "Unknown";
        if (!StringUtils.isNullOrEmpty(data.getStatus())) {
            status = data.getStatus();
        }
        drawString(stack, this.font, "Status: " + status, 10, baseInfoY + (this.font.FONT_HEIGHT + 2) * infoOffset, 0xfefefe);
        infoOffset++;

        // Progress
        if (!StringUtils.isNullOrEmpty(data.getProgress())) {
            String progress = data.getProgress();
            drawString(stack, this.font, "Progress: " + progress, 10, baseInfoY + (this.font.FONT_HEIGHT + 2) * infoOffset, 0xfefefe);
            infoOffset++;
        }

        // Message
        int y = baseInfoY + (this.font.FONT_HEIGHT + 2) * infoOffset;
        for (String s : data.getMessage().split("\n")) {
            drawString(stack, this.font, s, 11, y, 0xfefefe);
            y += this.font.FONT_HEIGHT + 2;
        }

        drawString(stack, this.font, this.playerInventory.getDisplayName(), 6, 129, 0xfefefe);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int x, int y) {
        this.renderBackground(stack);
        this.minecraft.textureManager.bindTexture(GUI);
        this.blit(stack, this.guiLeft, this.guiTop, 0, 0, 256, 256);
    }
}
