package com.ticticboooom.mods.mm.client.ter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.client.MMRenderTypes;
import com.ticticboooom.mods.mm.helper.NBTHelper;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;

public class StructureGenTileEntityRenderer extends TileEntityRenderer<StructureGenBlockEntity> {
    public StructureGenTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(StructureGenBlockEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemStack stackInSlot = tileEntityIn.getHandler().getStackInSlot(0);
        if (stackInSlot.isEmpty()) {
            return;
        }

        if (stackInSlot.getItem() != MMSetup.STRUCTURE_DEVICE.get()) {
           return;
        }

        if (stackInSlot.getTag() == null){
            return;
        }

        if (stackInSlot.getTag().contains("pos1") && stackInSlot.getTag().contains("pos2")) {
            CompoundNBT pos1c = stackInSlot.getTag().getCompound("pos1");
            CompoundNBT pos2c = stackInSlot.getTag().getCompound("pos2");

            BlockPos pos1 = NBTHelper.fromCompound(pos1c);
            BlockPos pos2 = NBTHelper.fromCompound(pos2c);
            BlockPos tilePos = tileEntityIn.getPos();

            pos1 = pos1.subtract(tilePos);
            pos2 = pos2.subtract(tilePos);

            IVertexBuilder builder = bufferIn.getBuffer(MMRenderTypes.OUTLINE);
            matrixStackIn.push();
            Matrix4f mat = matrixStackIn.getLast().getMatrix();

            int x = Math.min(pos1.getX(), pos2.getX());
            int y = Math.min(pos1.getY(), pos2.getY());
            int z = Math.min(pos1.getZ(), pos2.getZ());
            int dx = Math.max(pos1.getX(), pos2.getX()) + 1;
            int dy = Math.max(pos1.getY(), pos2.getY()) + 1;
            int dz = Math.max(pos1.getZ(), pos2.getZ()) + 1;

            int R = 255, G = 255, B = 255, A = 70;
            builder.pos(mat, x , y, z).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, y, z).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, y, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, x, y, dz).color(R, G, B, A).endVertex();

            builder.pos(mat, x , dy, z).color(R, G, B, A).endVertex();
            builder.pos(mat, x, dy, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, z).color(R, G, B, A).endVertex();

            builder.pos(mat, x , y, z).color(R, G, B, A).endVertex();
            builder.pos(mat, x, dy, z).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, z).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, y, z).color(R, G, B, A).endVertex();

            builder.pos(mat, x , y, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, y, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, x, dy, dz).color(R, G, B, A).endVertex();

            builder.pos(mat, dx , y, z).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, y, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, dx, dy, z).color(R, G, B, A).endVertex();

            builder.pos(mat, x , y, z).color(R, G, B, A).endVertex();
            builder.pos(mat, x, y, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, x, dy, dz).color(R, G, B, A).endVertex();
            builder.pos(mat, x, dy, z).color(R, G, B, A).endVertex();

            matrixStackIn.pop();
        }
    }
}
