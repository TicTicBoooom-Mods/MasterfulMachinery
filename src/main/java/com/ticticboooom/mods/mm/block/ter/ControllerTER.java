package com.ticticboooom.mods.mm.block.ter;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.client.model.data.EmptyModelData;

public class ControllerTER extends TileEntityRenderer<ControllerTile> {
    public ControllerTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ControllerTile tile, float partialTicks, MatrixStack mat, IRenderTypeBuffer buff, int combinedLightIn, int combinedOverlayIn) {
    }
}
