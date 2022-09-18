package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.cap.Capabilities;
import com.ticticboooom.mods.mm.cap.IBlueprintData;
import com.ticticboooom.mods.mm.client.container.ProjectorContainer;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Optional;

public class ProjectorScreen extends ContainerScreen<ProjectorContainer> {
    private final ProjectorContainer screenContainer;
    private static final ResourceLocation GUI = new ResourceLocation(Ref.MOD_ID, "textures/gui/port_gui.png");
    private static final ResourceLocation SLOT_PARTS = new ResourceLocation(Ref.MOD_ID, "textures/gui/slot_parts.png");


    public ProjectorScreen(ProjectorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.screenContainer = screenContainer;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.renderBackground(matrixStack);
        Minecraft.getInstance().textureManager.bindTexture(GUI);
        this.blit(matrixStack, this.guiLeft, this.guiTop - 20, 0, 0, 175, 256);
        Minecraft.getInstance().textureManager.bindTexture(SLOT_PARTS);
        blit(matrixStack, guiLeft + 151, guiTop - 10, 0, 26, 18, 18);

        ItemStack blueprint = screenContainer.tile.blueprint.getStackInSlot(0);
        if (!blueprint.isEmpty()) {
            Optional<IBlueprintData> structure = blueprint.getCapability(Capabilities.BLUEPRINT_DATA).resolve();
            if (structure.isPresent() && structure.get().getStructure() != null) {
                StructureModel structureModel = DataRegistry.STRUCTURES.get(structure.get().getStructure());
                drawString(matrixStack, Minecraft.getInstance().fontRenderer, structureModel.name, 0x444444, this.guiLeft + 5, this.guiTop + 5);
            }
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float p_230430_4_) {
        super.render(stack, mouseX, mouseY, p_230430_4_);

        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

    }
}
