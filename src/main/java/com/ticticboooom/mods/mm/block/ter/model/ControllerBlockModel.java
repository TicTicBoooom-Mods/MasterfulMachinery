package com.ticticboooom.mods.mm.block.ter.model;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.FlywheelClient;
import com.jozufozu.flywheel.core.QuadConverter;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.contraptions.components.flywheel.FlyWheelInstance;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.util.ControllerHelper;
import com.ticticboooom.mods.mm.util.ModelTools;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.print.attribute.standard.Sides;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControllerBlockModel implements IDynamicBakedModel {

    public static final ModelProperty<ControllerModel> CONTROLLER = new ModelProperty<>();
    public static final ControllerBlockModel INSTANCE = new ControllerBlockModel();
    public static final ControllerItemOverrideList OVERRIDE_LIST = new ControllerItemOverrideList();

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData data) {
        List<BakedQuad> quads = getModel(data).getQuads(state, side, rand, data);
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
