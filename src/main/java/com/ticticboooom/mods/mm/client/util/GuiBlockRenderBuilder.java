package com.ticticboooom.mods.mm.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mekanism.client.model.BaseModelCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.ArrayList;
import java.util.List;

public class GuiBlockRenderBuilder {
    private final BlockState blockState;
    private BlockPos position;
    private List<Quaternion> orderedRotation = new ArrayList<>();
    private Minecraft mc = Minecraft.getInstance();
    private Vector3f scale;
    private Vector3f prePosition = new Vector3f();

    public GuiBlockRenderBuilder(BlockState blockState) {
        this.blockState = blockState;
        position = new BlockPos(0, 0, 0);
    }

    public GuiBlockRenderBuilder at(BlockPos position) {
        this.position = position;
        return this;
    }

    public GuiBlockRenderBuilder withRotation(Quaternion rotation) {
        orderedRotation.add(rotation);
        return this;
    }

    public GuiBlockRenderBuilder withScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }

    public GuiBlockRenderBuilder withPrePosition(Vector3f position) {
        this.prePosition = position;
        return this;
    }

    private void prepareRender() {
        RenderSystem.enableBlend();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderHelper.setupFor3DItems();
        RenderSystem.alphaFunc(516, 0.1F);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void cleanupRender() {
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
    }

    public void finalize(MatrixStack ms) {
        prepareRender();
        IRenderTypeBuffer.Impl buf = mc.renderBuffers().bufferSource();
        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        ms.pushPose();
        transformMatrix(ms);
        brd.renderBlock(blockState, ms, buf, 0xF000F0, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        buf.endBatch();
        ms.popPose();
        cleanupRender();
    }

    private void transformMatrix(MatrixStack ms) {
        ms.scale(scale.x(), scale.y(), scale.z());
        ms.translate(prePosition.x(), prePosition.y(), prePosition.z());
        for (Quaternion quaternion : orderedRotation) {
            ms.mulPose(quaternion);
        }
        ms.translate(position.getX(), position.getY(), position.getZ());
    }
}
