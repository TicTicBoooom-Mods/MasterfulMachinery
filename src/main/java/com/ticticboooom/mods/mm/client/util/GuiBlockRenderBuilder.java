package com.ticticboooom.mods.mm.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ticticboooom.mods.mm.client.jei.category.render.AirBlockReader;
import mekanism.client.model.BaseModelCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
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
    private TileEntityRenderer<TileEntity> ter = null;
    private TileEntity tile;

    public GuiBlockRenderBuilder(BlockState blockState) {
        this.blockState = blockState;
        position = new BlockPos(0, 0, 0);
        try {

            AirBlockReader airBlockReader = new AirBlockReader(blockState);
            tile = blockState.createTileEntity(airBlockReader);
            airBlockReader.setTile(tile);
            if (tile != null) {
                ter = TileEntityRendererDispatcher.instance.getRenderer(tile);
            }
        } catch (Exception ignored) {
        }
    }

    public GuiBlockRenderBuilder at(BlockPos position) {
        if (tile != null) {
            tile.setWorldAndPos(Minecraft.getInstance().world, BlockPos.ZERO);
        }
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
        RenderHelper.setupGui3DDiffuseLighting();
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
        IRenderTypeBuffer.Impl buf = mc.getRenderTypeBuffers().getBufferSource();
        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
        ms.push();
        transformMatrix(ms);

        brd.renderBlock(blockState, ms, buf, 0xF000F0, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        ms.push();
        try {
            if (ter != null) {
                ter.render(tile, 1.f, ms, buf, 0xF000F0, OverlayTexture.NO_OVERLAY);
            }
        } catch (Exception ignored) {
        }
        buf.finish();
        ms.pop();
        ms.pop();
        cleanupRender();
    }

    private void transformMatrix(MatrixStack ms) {
        ms.scale(12, -12, 12);
        ms.translate(prePosition.getX(), prePosition.getY(), prePosition.getZ());
        for (Quaternion quaternion : orderedRotation) {
            ms.rotate(quaternion);
        }
        ms.scale(scale.getX(), -scale.getY(), scale.getZ());
        ms.translate(position.getX(), position.getY(), position.getZ());
        if (tile != null) {
            tile.setPos(new BlockPos(0, 0, 0));
        }
    }
}
