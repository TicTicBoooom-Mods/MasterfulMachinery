package com.ticticboooom.mods.mm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.client.screen.ControllerBlockContainerScreen;
import com.ticticboooom.mods.mm.client.screen.PortBlockContainerScreen;
import com.ticticboooom.mods.mm.datagen.MMPackFinder;
import com.ticticboooom.mods.mm.datagen.MemoryDataGeneratorFactory;
import com.ticticboooom.mods.mm.datagen.PackType;
import com.ticticboooom.mods.mm.datagen.gen.MMBlockStateProvider;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMPorts;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
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


    private DataGenerator generator;
    private static boolean hasGenerated = false;
    private static MM instance;

    public MM() {
        instance = this;
        MMPorts.init();
        MMLoader.load();
        MemoryDataGeneratorFactory.init();
        registerDataGen();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MMLoader.BLOCKS_REG.register(bus);
        MMLoader.ITEMS_REG.register(bus);
        MMLoader.TILES_REG.register(bus);
        MMLoader.CONTAINER_REG.register(bus);
        RecipeTypes.RECIPE_SERIALIZERS.register(bus);
        bus.addListener(this::clientEvents);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            Minecraft.getInstance().getResourcePackRepository().addPackFinder(new MMPackFinder(PackType.RESOURCE));
        }
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
    }

    private void registerDataGen() {
        generator = MemoryDataGeneratorFactory.createMemoryDataGenerator();
        ExistingFileHelper existingFileHelper = new ExistingFileHelper(ImmutableList.of(), ImmutableSet.of(), false);

        generator.addProvider(new MMBlockStateProvider(generator, existingFileHelper));
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
        event.getServer().getPackRepository().addPackFinder(new MMPackFinder(PackType.DATA));
    }

    private void clientEvents(final FMLClientSetupEvent event) {

        for (RegistryObject<ContainerType<ControllerBlockContainer>> container : MMLoader.CONTAINERS) {
            ScreenManager.register(container.get(), ControllerBlockContainerScreen::new);
        }
        for (RegistryObject<ContainerType<PortBlockContainer>> container : MMLoader.PORT_CONTAINERS) {
            ScreenManager.register(container.get(), PortBlockContainerScreen::new);
        }
        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.solid() || layer == RenderType.translucent());
        }

        for (RegistryObject<MachinePortBlock> block : MMLoader.IPORT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.solid() || layer == RenderType.translucent());
        }

        for (RegistryObject<MachinePortBlock> block : MMLoader.OPORT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(block.get(), layer -> layer == RenderType.solid() || layer == RenderType.translucent());
        }
    }
}
