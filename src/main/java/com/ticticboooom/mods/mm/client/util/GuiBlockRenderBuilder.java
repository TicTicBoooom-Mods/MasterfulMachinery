package com.ticticboooom.mods.mm.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ticticboooom.mods.mm.client.jei.category.render.AirBlockReader;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
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
    private Vector3f positionOffset = new Vector3f();
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
            tile.setWorldAndPos(Minecraft.getInstance().world, Minecraft.getInstance().player.getPosition());
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

    public GuiBlockRenderBuilder withOffset(Vector3f offset) {
        this.positionOffset = offset;
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

        //brd.renderBlock(blockState, ms, buf, 0xF000F0, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

        // Copied the code to render block using a model from BlockRenderDispatcher#renderBlock for the case of model.
        // Some blocks use ENTITYBLOCK_ANIMATED which is weird and I dont know how it works coz it only ever works on vanilla items,
        // and I just dont know what to think about this honestly but this seems to work so I'm not touching it anymore unless something
        // else decides to break in which case I will indeed come back to this and figure out a work around then.
        IBakedModel model = brd.getModelForState(blockState);
        int color = Minecraft.getInstance().getBlockColors().getColor(blockState, null, null, 0);
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        brd.getBlockModelRenderer().renderModel(ms.getLast(), buf.getBuffer(RenderTypeLookup.func_239220_a_(blockState, false)), blockState, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);


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
        ms.translate(position.getX() + positionOffset.getX(), position.getY() + positionOffset.getY(), position.getZ() + positionOffset.getZ());
        if (tile != null) {
            //tile.setPos(new BlockPos(0, 0, 0));
        }
    }
}
