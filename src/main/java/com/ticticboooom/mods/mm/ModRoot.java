package com.ticticboooom.mods.mm;

import com.ticticboooom.mods.mm.block.ter.model.controller.ControllerBlockModel;
import com.ticticboooom.mods.mm.block.ter.model.port.PortBlockModel;
import com.ticticboooom.mods.mm.client.screen.ControllerScreen;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.ports.PortTypeRegistry;
import com.ticticboooom.mods.mm.ports.base.PortType;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.setup.MMContainerTypes;
import com.ticticboooom.mods.mm.setup.MMItems;
import com.ticticboooom.mods.mm.setup.MMTiles;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;

@Mod(Ref.MOD_ID)
public class ModRoot {


    public ModRoot() {
        PortTypeRegistry.registerDefault();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MMTiles.TILES.register(bus);
        MMBlocks.BLOCKS.register(bus);
        MMItems.ITEMS.register(bus);
        MMContainerTypes.CONTAINERS.register(bus);
        bus.addListener(ModRoot::commonSetup);

        if (EffectiveSide.get().isClient()) {
            bus.addListener(ModRoot::modelRegistry);
            bus.addListener(ModRoot::bake);
            bus.addListener(ModRoot::clientSetup);
            bus.addListener(ModRoot::stitch);
        }
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(MMNetworkManager::init);
    }

    public static void bake(ModelBakeEvent event) {
        MMBlocks.CONTROLLER.get().getStateContainer().getValidStates().forEach(blockState -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(blockState);
            event.getModelRegistry().put(modelLocation, new ControllerBlockModel());
        });
        event.getModelRegistry().put(new ModelResourceLocation(MMItems.CONTROLLER.getId(), "inventory"), new ControllerBlockModel());
        MMBlocks.PORT.get().getStateContainer().getValidStates().forEach(b -> {
            ModelResourceLocation modelLocation = BlockModelShapes.getModelLocation(b);
            event.getModelRegistry().put(modelLocation, new PortBlockModel());
        });
        event.getModelRegistry().put(new ModelResourceLocation(MMItems.PORT.getId(), "inventory"), new PortBlockModel());
    }

    public static void stitch(TextureStitchEvent.Pre event) {
        event.addSprite(Ref.res("block/controller_cutout"));
        for (Map.Entry<ResourceLocation, PortType> entry : PortTypeRegistry.PORT_TYPES.entrySet()) {
            event.addSprite(entry.getValue().getInputCutout());
            event.addSprite(entry.getValue().getOutputCutout());
        }
    }

    public static void modelRegistry(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(MMBlocks.CONTROLLER.getId());
        ModelLoader.addSpecialModel(MMBlocks.PORT.getId());
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(MMBlocks.CONTROLLER.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(MMBlocks.PORT.get(), RenderType.getTranslucent());
            ScreenManager.registerFactory(MMContainerTypes.CONTROLLER.get(), ControllerScreen::new);
        });
    }
}
