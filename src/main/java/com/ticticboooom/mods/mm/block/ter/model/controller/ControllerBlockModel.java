package com.ticticboooom.mods.mm.block.ter.model.controller;

import com.google.common.collect.Lists;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.util.ModelTools;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class ControllerBlockModel implements IDynamicBakedModel {

    public static final ModelProperty<ControllerModel> CONTROLLER = new ModelProperty<>();
    public static final ControllerItemOverrideList OVERRIDE_LIST = new ControllerItemOverrideList();

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        List<BakedQuad> quads = Lists.newArrayList(getModel(data).getQuads(state, side, rand, data));
        if (state != null && side == state.get(DirectionalBlock.FACING)) {
            if (data.hasProperty(CONTROLLER) && data.getData(CONTROLLER) != null && Objects.requireNonNull(data.getData(CONTROLLER)).showCutout) {
                quads.add(ModelTools.createQuad(Ref.res("block/controller_cutout"), side));
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
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data) {
        return getModel(data).getParticleTexture(data);
    }


    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.getParticleTexture(EmptyModelData.INSTANCE);
    }

    @Override
    public ItemOverrideList getOverrides() {
        return OVERRIDE_LIST;
    }

    private IBakedModel getModel(IModelData data) {
        if (!data.hasProperty(CONTROLLER) || data.getData(CONTROLLER) == null) {
            return Minecraft.getInstance().getModelManager().getMissingModel();
        }
        ControllerModel controller = data.getData(CONTROLLER);
        IBakedModel model = controller.defaultModel.getModel();
        return model;
    }
}
