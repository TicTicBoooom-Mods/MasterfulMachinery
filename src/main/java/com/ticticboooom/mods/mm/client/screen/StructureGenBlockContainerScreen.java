package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.StructureGenBlockContainer;
import com.ticticboooom.mods.mm.helper.StructureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class StructureGenBlockContainerScreen extends ContainerScreen<StructureGenBlockContainer> {
    private final StructureGenBlockContainer cont;

    public StructureGenBlockContainerScreen(StructureGenBlockContainer cont, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(cont, p_i51105_2_, p_i51105_3_);
        this.cont = cont;
    }

    @Override
    public void render(MatrixStack ms, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(ms, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
        this.minecraft.fontRenderer.func_238418_a_(new StringTextComponent("Structure Generator"), 10, -10,   176, 0xfefefe);
        drawString(ms, this.minecraft.fontRenderer, "Inventory", 7, 107, 0xfefefe);
        drawCenteredString(ms, this.minecraft.fontRenderer, "COPY TO CLIPBOARD", 80, 50, 0xfefefe);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
        this.renderBackground(ms);
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        blit(ms, guiLeft, guiTop - 20, 0, 0, 175, 256);

        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(MM.ID, "textures/gui/slot_parts.png"));
        blit(ms, guiLeft + 150, guiTop  - 15, 0, 26, 18, 18);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int)mouseX - guiLeft;
        int y = (int)mouseY - guiTop;

        if (x > 0 && x < 176 && y > 40 && y < 60){
            StructureHelper.copyToClickBoard(cont.getTile().getInv().getStackInSlot(0).getTag(), cont.getTile().getWorld());
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
