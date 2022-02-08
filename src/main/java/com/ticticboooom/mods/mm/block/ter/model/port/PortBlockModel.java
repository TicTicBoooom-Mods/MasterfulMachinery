package com.ticticboooom.mods.mm.block.ter.model.port;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.ports.PortTypeRegistry;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.util.ModelTools;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class PortBlockModel implements IDynamicBakedModel {
    public static final ModelProperty<PortModel> PORT = new ModelProperty<>();
    public static final ModelProperty<Boolean> PORT_IO_TYPE = new ModelProperty<>();
    public static final PortItemOverrideList OVERRIDE_LIST = new PortItemOverrideList();

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        List<BakedQuad> quads = Lists.newArrayList(getModel(data).getQuads(state, side, rand, data));
        if (side == null) {
            return quads;
        }
        PortModel port = data.getData(PORT);
        if (data.hasProperty(PORT) && port != null) {
            PortType portType = PortTypeRegistry.PORT_TYPES.get(port.type);
            if (data.hasProperty(PORT_IO_TYPE)) {
                Boolean isInput = data.getData(PORT_IO_TYPE);
                if (isInput != null && isInput) {
                    quads.add(ModelTools.createQuad(portType.getInputCutout(), side));
                } else {
                    quads.add(ModelTools.createQuad(portType.getOutputCutout(), side));
                }
            }
        }
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isSideLit() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return getModel(data).getParticleTexture(data);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return OVERRIDE_LIST;
    }

    private IBakedModel getModel(IModelData data) {
        if (!data.hasProperty(PORT) || data.getData(PORT) == null) {
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
        PortModel port = data.getData(PORT);
        IBakedModel model = port.defaultModel.getModel();
        return model;
    }
}
