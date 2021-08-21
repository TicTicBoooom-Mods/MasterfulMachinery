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

            float x = Math.min(pos1.getX(), pos2.getX());
            float y = Math.min(pos1.getY(), pos2.getY());
            float z = Math.min(pos1.getZ(), pos2.getZ());
            float dx = Math.max(pos1.getX(), pos2.getX()) + 1;
            float dy = Math.max(pos1.getY(), pos2.getY()) + 1;
            float dz = Math.max(pos1.getZ(), pos2.getZ()) + 1;
            float xAdjust = addWiggleRoom(x,dx);
            x = x+xAdjust;
            dx = dx+(xAdjust*-1);
            float yAdjust = addWiggleRoom(y,dy);
            y = y+yAdjust;
            dy = dy+(yAdjust*-1);
            float zAdjust = addWiggleRoom(z,dz);
            z = z+zAdjust;
            dz = dz+(zAdjust*-1);

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

    private float addWiggleRoom(float coordOne, float coordTwo) {
        if(coordOne > coordTwo) {
            return 0.05f;
        }
        return -0.05f;
    }
}
