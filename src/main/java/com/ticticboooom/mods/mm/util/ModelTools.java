package com.ticticboooom.mods.mm.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.common.model.TransformationHelper;

public class ModelTools {

    private static void putVertex(BakedQuadBuilder b, Vector3f normal, Vector4f vec, float u, float v, TextureAtlasSprite sprite) {
        ImmutableList<VertexFormatElement> elems = b.getVertexFormat().getElements().asList();
        for (int i = 0; i < elems.size(); i++) {
            VertexFormatElement e = elems.get(i);
            switch (e.getUsage()) {
                case POSITION:
                    b.put(i, vec.getX(), vec.getY(), vec.getZ(), vec.getW());
                    break;
                case COLOR:
                    b.put(i, 1f, 1f, 1f, 1f);
                    break;
                case UV:
                    putVertexUV(b, u, v, sprite, i, e);
                    break;
                case NORMAL:
                    b.put(i, normal.getX(), normal.getY(), normal.getZ());
                    break;
                default:
                    b.put(i);
                    break;
            }
        }
    }

    private static void putVertexUV(BakedQuadBuilder builder, float u, float v, TextureAtlasSprite sprite, int j, VertexFormatElement e) {
        switch (e.getIndex()) {
            case 0:
                builder.put(j, sprite.getInterpolatedU(u), sprite.getInterpolatedV(v));
                break;
            case 2:
                builder.put(j, (short) 0, (short) 0);
                break;
            default:
                builder.put(j);
                break;
        }
    }

    public static BakedQuad createQuad(ResourceLocation texture, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Direction side) {
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
        b.setQuadOrientation(side);
        return b.build();
    }

    public static BakedQuad createQuad(ResourceLocation texture, Direction side) {
        if (side == Direction.NORTH){
            return createQuad(texture, new Vector3f(1, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), side);
        }
        else if (side == Direction.SOUTH) {
            return createQuad(texture, new Vector3f(0, 1, 1), new Vector3f(0, 0, 1), new Vector3f(1, 0, 1), new Vector3f(1, 1, 1), side);
        }
        else if (side == Direction.WEST) {
            return createQuad(texture, new Vector3f(0, 1, 0), new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 1), side);
        }
        else if (side == Direction.EAST) {
            return createQuad(texture, new Vector3f(1, 1, 1), new Vector3f(1, 0, 1), new Vector3f(1, 0, 0), new Vector3f(1, 1, 0), side);
        }
        else if (side == Direction.UP) {
            return createQuad(texture, new Vector3f(0, 1, 0), new Vector3f(0, 1, 1), new Vector3f(1, 1, 0), new Vector3f(1, 1, 1), side);
        }
        else if (side == Direction.DOWN) {
            return createQuad(texture, new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), side);
        }
        return createQuad(texture, new Vector3f(1, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), side);
    }

}
