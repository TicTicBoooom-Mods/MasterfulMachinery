package com.ticticboooom.mods.mm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.client.screen.ControllerBlockContainerScreen;
import com.ticticboooom.mods.mm.client.screen.PortBlockContainerScreen;
import com.ticticboooom.mods.mm.client.screen.StructureGenBlockContainerScreen;
import com.ticticboooom.mods.mm.client.ter.StructureGenTileEntityRenderer;
import com.ticticboooom.mods.mm.datagen.MMPackFinder;
import com.ticticboooom.mods.mm.datagen.MemoryDataGeneratorFactory;
import com.ticticboooom.mods.mm.datagen.PackType;
import com.ticticboooom.mods.mm.datagen.gen.MMBlockStateProvider;
import com.ticticboooom.mods.mm.datagen.gen.MMItemModelProvider;
import com.ticticboooom.mods.mm.datagen.gen.MMLangProvider;
import com.ticticboooom.mods.mm.datagen.gen.MMLootTableProvider;
import com.ticticboooom.mods.mm.network.PacketHandler;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMPorts;
import com.ticticboooom.mods.mm.registration.MMSetup;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.resources.ResourcePackList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(MM.ID)
public class MM {
    public static final String ID = "masterfulmachinery";
    public static final Logger LOG = LogManager.getLogger("Masterful Machinery");
    public static final Gson GSON = new Gson();

    private DataGenerator generator;
    private static boolean hasGenerated = false;
    private static MM instance;

    public MM() {
        instance = this;
        MMPorts.init();
        MMLoader.load();
        MemoryDataGeneratorFactory.init();
        PacketHandler.init();
        registerDataGen();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MMSetup.BLOCKS_REG.register(bus);
        MMSetup.ITEMS_REG.register(bus);
        MMSetup.TILES_REG.register(bus);
        MMSetup.CONTAINER_REG.register(bus);
        RecipeTypes.RECIPE_SERIALIZERS.register(bus);
        bus.addListener(this::clientEvents);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().getResourcePackList().addPackFinder(new MMPackFinder(PackType.RESOURCE));
        }
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
    }

    private void registerDataGen() {
        generator = MemoryDataGeneratorFactory.createMemoryDataGenerator();
        ExistingFileHelper existingFileHelper = new ExistingFileHelper(ImmutableList.of(), ImmutableSet.of(), false);
        generator.addProvider(new MMLootTableProvider(generator));

        if (FMLEnvironment.dist != Dist.DEDICATED_SERVER){
            generator.addProvider(new MMBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(new MMItemModelProvider(generator, existingFileHelper));
            generator.addProvider(new MMLangProvider(generator));
        }
    }

    public static void generate() {
        if(!hasGenerated) {
            try {
                instance.generator.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            hasGenerated = true;
        }
    }

    public void onServerStart(final FMLServerAboutToStartEvent event) {
        event.getServer().getResourcePacks().addPackFinder(new MMPackFinder(PackType.DATA));
    }

    private void clientEvents(final FMLClientSetupEvent event) {

        for (RegistryObject<ContainerType<ControllerBlockContainer>> container : MMLoader.CONTAINERS) {
            ScreenManager.registerFactory(container.get(), ControllerBlockContainerScreen::new);
        }

        for (RegistryObject<ContainerType<?>> container : MMLoader.PORT_CONTAINERS) {
            ScreenManager.registerFactory((ContainerType<PortBlockContainer>) container.get(), PortBlockContainerScreen::new);
        }

        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
        }

        for (RegistryObject<MachinePortBlock> block : MMLoader.IPORT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
        }

        for (RegistryObject<MachinePortBlock> block : MMLoader.OPORT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
        }


        RenderTypeLookup.setRenderLayer(MMSetup.PROJECTOR_BLOCK.get(), RenderType.getTranslucent());
        ScreenManager.registerFactory(MMSetup.STRUCTURE_CONTAINER.get(), StructureGenBlockContainerScreen::new);
        ClientRegistry.bindTileEntityRenderer(MMSetup.STRUCTURE_TILE.get(), StructureGenTileEntityRenderer::new);
    }

    public static void injectDatapackFinder (ResourcePackList resourcePacks) {
        if (DistExecutor.unsafeRunForDist( () -> () -> resourcePacks != Minecraft.getInstance().getResourcePackList(), () -> () -> true)) {
            resourcePacks.addPackFinder(new MMPackFinder(PackType.RESOURCE));
            MM.LOG.info("Injecting data pack finder.");
        }
    }
}
