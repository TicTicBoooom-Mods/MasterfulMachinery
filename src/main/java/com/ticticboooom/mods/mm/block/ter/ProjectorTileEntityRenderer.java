package com.ticticboooom.mods.mm.block.ter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.block.tile.ProjectorTile;
import com.ticticboooom.mods.mm.cap.Capabilities;
import com.ticticboooom.mods.mm.cap.IBlueprintData;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Optional;

public class ProjectorTileEntityRenderer extends TileEntityRenderer<ProjectorTile> {

    public ProjectorTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ProjectorTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack blueprint = tileEntityIn.blueprint.getStackInSlot(0);
        if (blueprint == null || blueprint.isEmpty()) {
            return;
        }

        Optional<IBlueprintData> bp = blueprint.getCapability(Capabilities.BLUEPRINT_DATA).resolve();
        if (!bp.isPresent()) {
            return;
        }
        ResourceLocation structure = bp.get().getStructure();
        StructureModel model = DataRegistry.STRUCTURES.get(structure);
        if (model == null){
            return;
        }
        BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();

        BlockPos pos = tileEntityIn.getPos();

        for (StructureModel.PositionedKey key : model.positionedKeys) {
            StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(key.type);
            value.onBlueprintInitialRender(pos.add(key.pos), model, key.data);
            GuiBlockRenderBuilder guiBlockRenderBuilder = value.onBlueprintRender(pos.add(key.pos), model, key.data);
            BlockState blockState = guiBlockRenderBuilder.blockState;
            TileEntity tile = guiBlockRenderBuilder.tile;
            IBakedModel mdl = brd.getModelForState(blockState);

            int color = Minecraft.getInstance().getBlockColors().getColor(blockState, null, null, 0);
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            matrixStackIn.push();
            matrixStackIn.translate(key.pos.getX(), key.pos.getY(), key.pos.getZ());
            matrixStackIn.scale(0.7f, 0.7f, 0.7f);
            brd.getBlockModelRenderer().renderModel(matrixStackIn.getLast(), bufferIn.getBuffer(RenderTypeLookup.func_239220_a_(blockState, false)), blockState, mdl, r,g,b, 0xF000F0, OverlayTexture.NO_OVERLAY, tile != null ? tile.getModelData() : EmptyModelData.INSTANCE);
            matrixStackIn.pop();
        }
    }
}
