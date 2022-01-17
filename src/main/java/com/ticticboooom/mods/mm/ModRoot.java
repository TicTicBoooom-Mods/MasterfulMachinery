package com.ticticboooom.mods.mm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.ticticboooom.mods.mm.block.ter.model.ControllerBlockModel;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.setup.MMItems;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Ref.MOD_ID)
public class ModRoot {
    public ModRoot() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MMTiles.TILES.register(bus);
        MMBlocks.BLOCKS.register(bus);
        MMItems.ITEMS.register(bus);

        if (EffectiveSide.get().isClient()) {
            bus.addListener(ModRoot::modelRegistry);
            bus.addListener(ModRoot::bake);
            bus.addListener(ModRoot::clientSetup);
            bus.addListener(ModRoot::stitch);
        }
    }

    public static void bake(ModelBakeEvent event) {
        MMBlocks.CONTROLLER.get().getStateContainer().getValidStates().forEach(blockState -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(blockState);
            event.getModelRegistry().put(modelLocation, ControllerBlockModel.INSTANCE);
        });
        event.getModelRegistry().put(new ModelResourceLocation(MMItems.CONTROLLER.getId(), "inventory"), ControllerBlockModel.INSTANCE);
    }

    public static void stitch(TextureStitchEvent.Pre event) {
        event.addSprite(Ref.res("block/controller_cutout"));
    }

    public static void modelRegistry(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(MMBlocks.CONTROLLER.getId());
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(MMBlocks.CONTROLLER.get(), RenderType.getTranslucent());
        });
    }
}
