package com.ticticboooom.mods.mm.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

public class ModelTools {

    private static void putVertex(BakedQuadBuilder b, Vector3f normal, Vector4f vec, float u, float v, TextureAtlasSprite sprite) {
        ImmutableList<VertexFormatElement> elems = b.getVertexFormat().getElements().asList();
        for (int i = 0; i < elems.size(); i++) {
            VertexFormatElement e = elems.get(i);
            switch (e.getUsage()) {
                case POSITION: b.put(i, vec.getX(), vec.getY(), vec.getZ(), 1.0f);
                case COLOR: b.put(i, 1f, 1f, 1f, 1f);
                case UV: putVertexUV(b, u, v, sprite, i, e);
                case NORMAL: b.put(i, normal.getX(), normal.getY(), normal.getZ(), 0f);
            }
        }
    }

    private static void putVertexUV(BakedQuadBuilder builder, float u, float v, TextureAtlasSprite sprite, int j, VertexFormatElement e) {
        switch (e.getIndex()) {
            case 0: builder.put(j, sprite.getInterpolatedU(u), sprite.getInterpolatedV(v));
            case 2:  builder.put(j, (short) 0, (short) 0);
            default: builder.put(j);
        }
    }

    public static BakedQuad createQuad(ResourceLocation texture, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4) {
        Vector3f normal = v3.copy();
        normal.sub(v2);
        Vector3f tmp = v1.copy();
        tmp.sub(v2);
        normal.cross(tmp);
        normal.normalize();
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(texture);

        int tw = sprite.getWidth();
        int th = sprite.getHeight();

        BakedQuadBuilder b = new BakedQuadBuilder(sprite);
        putVertex(b, normal, new Vector4f(v1), 0, 0, sprite);
        putVertex(b, normal, new Vector4f(v2), 0, th, sprite);
        putVertex(b, normal, new Vector4f(v3), tw, th, sprite);
        putVertex(b, normal, new Vector4f(v4), tw, 0, sprite);
        b.setApplyDiffuseLighting(true);
        return b.build();
    }
}
