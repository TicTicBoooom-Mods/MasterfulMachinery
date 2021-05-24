package com.ticticboooom.mods.mm.registration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.ControllerBlockEntity;
import com.ticticboooom.mods.mm.helper.IOHelper;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MMLoader {

    public static final ItemGroup MASTERFUL_ITEM_GROUP = new ItemGroup(MM.ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MMSetup.BLUEPRINT.get());
        }
    };

    public static final ArrayList<RegistryObject<ControllerBlock>> BLOCKS = new ArrayList<>();
    public static final ArrayList<RegistryObject<MachinePortBlock>> IPORT_BLOCKS = new ArrayList<>();
    public static final ArrayList<RegistryObject<MachinePortBlock>> OPORT_BLOCKS = new ArrayList<>();
    public static final ArrayList<RegistryObject<ContainerType<ControllerBlockContainer>>> CONTAINERS = new ArrayList<>();
    public static final ArrayList<RegistryObject<ContainerType<?>>> PORT_CONTAINERS = new ArrayList<>();

    public static void load() {
        Path rootPath = FMLPaths.CONFIGDIR.get().resolve("masterful_machinery");
        File rootFile = IOHelper.getFileAndCreate(rootPath);

        Path controllersPath = rootPath.resolve("controllers");
        File controllersFile = IOHelper.getFileAndCreate(controllersPath);

        List<JsonObject> load = load(controllersFile);

        for (JsonObject obj : load) {
            String controllerId = obj.get("controllerId").getAsString();
            String controllerName = obj.get("name").getAsString();
            String texOverride = null;
            if (obj.has("textureOverride")) {
                texOverride = obj.get("textureOverride").getAsString();
            }
            final String textureOverrideFinal = texOverride;
            {
                Registerable<RegistryObject<TileEntityType<?>>> controllerTile = new Registerable<>();
                Registerable<RegistryObject<ControllerBlock>> controllerBlock = new Registerable<>();
                Registerable<RegistryObject<ContainerType<ControllerBlockContainer>>> cont = new Registerable<>();
                cont.set(MMSetup.CONTAINER_REG.register(controllerId + "_controller", () -> IForgeContainerType.create((i, o, u) -> new ControllerBlockContainer(cont.get().get(), i, o, u))));
                controllerBlock.set(MMSetup.BLOCKS_REG.register(controllerId + "_controller", () -> new ControllerBlock(controllerTile.get(), controllerName, controllerId, textureOverrideFinal)));
                controllerTile.set(MMSetup.TILES_REG.register(controllerId + "_controller", () -> TileEntityType.Builder.create(() -> new ControllerBlockEntity(controllerTile.get(), cont.get(), controllerId), controllerBlock.get().get()).build(null)));
                MMSetup.ITEMS_REG.register(controllerId + "_controller", () -> new BlockItem(controllerBlock.get().get(), new Item.Properties().group(MASTERFUL_ITEM_GROUP)));
                BLOCKS.add(controllerBlock.get());
                CONTAINERS.add(cont.get());
            }

            JsonArray ports = obj.get("ports").getAsJsonArray();
            for (JsonElement port : ports) {
                JsonObject portObj = port.getAsJsonObject();
                String type = portObj.get("type").getAsString();
                String id = portObj.get("id").getAsString();
                String name = portObj.get("name").getAsString();
                String portTexOverride = null;
                if (portObj.has("textureOverride")){
                    portTexOverride = portObj.get("textureOverride").getAsString();
                } else {
                    portTexOverride = textureOverrideFinal;
                }
                final String portTextureOverrideFinal = portTexOverride;
                ResourceLocation resourceLocation = RLUtils.toRL(type);
                MasterfulPortType value = MMPorts.PORTS.get(resourceLocation);
                if (value == null){
                    MM.LOG.warn("port type not present: " + resourceLocation.toString());
                    continue;
                }
                Supplier<PortStorage> data = value.getParser().createStorage(portObj.get("data").getAsJsonObject());

                {
                    Registerable<RegistryObject<TileEntityType<?>>> tile = new Registerable<>();
                    Registerable<RegistryObject<MachinePortBlock>> block = new Registerable<>();
                    Registerable<RegistryObject<ContainerType<?>>> cont = new Registerable<>();
                    cont.set(MMSetup.CONTAINER_REG.register(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_input", () -> IForgeContainerType.create((i, o, u) -> new PortBlockContainer(cont.get().get(), i, o, u))));
                    block.set(value.getParser().registerBlock(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_input", MMSetup.BLOCKS_REG, tile, name, controllerId, portTextureOverrideFinal, value.getParser().getInputOverlay()));
                    tile.set(value.getParser().registerTileEntity(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_input", MMSetup.TILES_REG, tile, block, cont, data, true));
                    MMSetup.ITEMS_REG.register(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_input", () -> new BlockItem(block.get().get(), new Item.Properties().group(MASTERFUL_ITEM_GROUP)));
                    PORT_CONTAINERS.add(cont.get());
                    IPORT_BLOCKS.add(block.get());
                }

                {
                    Registerable<RegistryObject<TileEntityType<?>>> tile = new Registerable<>();
                    Registerable<RegistryObject<MachinePortBlock>> block = new Registerable<>();
                    Registerable<RegistryObject<ContainerType<?>>> cont = new Registerable<>();
                    cont.set(MMSetup.CONTAINER_REG.register(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_output", () -> IForgeContainerType.create((i, o, u) -> new PortBlockContainer(cont.get().get(), i, o, u))));
                    block.set(value.getParser().registerBlock(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_output", MMSetup.BLOCKS_REG, tile, name, controllerId, portTextureOverrideFinal, value.getParser().getOutputOverlay()));
                    tile.set(value.getParser().registerTileEntity(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_output", MMSetup.TILES_REG, tile, block, cont, data, false));
                    MMSetup.ITEMS_REG.register(controllerId + "_" + id + "_port_" + resourceLocation.getPath() + "_output", () -> new BlockItem(block.get().get(), new Item.Properties().group(MASTERFUL_ITEM_GROUP)));
                    PORT_CONTAINERS.add(cont.get());
                    OPORT_BLOCKS.add(block.get());
                }

            }
        }
    }

    private static List<JsonObject> load(File file) {
        if (!file.exists()) {
            MM.LOG.fatal("file non existent upon reading, where TF has it gone between listing and reading hm Hm HMMMMM?!?!?!?!?!");
            return new ArrayList<>();
        }
        ArrayList<JsonObject> result = new ArrayList<JsonObject>();
        File[] list = file.listFiles();

        if (list == null) {
            return result;
        }

        for (File s : list) {
            if (!s.exists()) {
                MM.LOG.fatal("file non existent upon reading, where TF has it gone between listing and reading hm Hm HMMMMM?!?!?!?!?!");
                continue;
            }
            try {
                FileReader fr = new FileReader(s);
                JsonElement json = new JsonParser().parse(fr);
                result.add(json.getAsJsonObject());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                MM.LOG.fatal("Failed to read or parse file: {}", file.getAbsolutePath());
            }
        }
        return result;
    }
}
