package com.ticticboooom.mods.mm.registration;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ProjectorBlock;
import com.ticticboooom.mods.mm.block.StructureGenBlock;
import com.ticticboooom.mods.mm.block.container.ProjectorBlockContainer;
import com.ticticboooom.mods.mm.block.container.StructureGenBlockContainer;
import com.ticticboooom.mods.mm.block.tile.ProjectorBlockEntity;
import com.ticticboooom.mods.mm.block.tile.StructureGenBlockEntity;
import com.ticticboooom.mods.mm.item.StructureGenSelectionDevice;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMSetup {

    public static final DeferredRegister<Block> BLOCKS_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, MM.ID);
    public static final DeferredRegister<Item> ITEMS_REG = DeferredRegister.create(ForgeRegistries.ITEMS, MM.ID);
    public static final DeferredRegister<TileEntityType<?>> TILES_REG = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MM.ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINER_REG = DeferredRegister.create(ForgeRegistries.CONTAINERS, MM.ID);

    public static final RegistryObject<Item> BLUEPRINT = ITEMS_REG.register("blueprint", () -> new Item(new Item.Properties().group(MMLoader.MASTERFUL_ITEM_GROUP)));

    public static final RegistryObject<TileEntityType<?>> PROJECTOR_TILE = TILES_REG.register("projector", () -> TileEntityType.Builder.create(ProjectorBlockEntity::new).build(null));
    public static final RegistryObject<Block> PROJECTOR_BLOCK = BLOCKS_REG.register("projector", ProjectorBlock::new);
    public static final RegistryObject<ContainerType<ProjectorBlockContainer>> PROJECTOR_CONTAINER = CONTAINER_REG.register("projector", () -> IForgeContainerType.create(ProjectorBlockContainer::new));
    public static final RegistryObject<Item> PROJECTOR_ITEM = ITEMS_REG.register("projector", () -> new BlockItem(PROJECTOR_BLOCK.get(), new Item.Properties().group(MMLoader.MASTERFUL_ITEM_GROUP)));


    public static final RegistryObject<Block> STRUCTURE_BLOCK = BLOCKS_REG.register("structure_generator", StructureGenBlock::new);
    public static final RegistryObject<TileEntityType<StructureGenBlockEntity>> STRUCTURE_TILE = TILES_REG.register("structure_generator", () -> TileEntityType.Builder.create(StructureGenBlockEntity::new, STRUCTURE_BLOCK.get()).build(null));
    public static final RegistryObject<ContainerType<StructureGenBlockContainer>> STRUCTURE_CONTAINER = CONTAINER_REG.register("structure_generator", () -> IForgeContainerType.create(StructureGenBlockContainer::new));
    public static final RegistryObject<Item> STRUCTURE_ITEM = ITEMS_REG.register("structure_generator", () -> new BlockItem(STRUCTURE_BLOCK.get(), new Item.Properties().group(MMLoader.MASTERFUL_ITEM_GROUP)));
    public static final RegistryObject<Item> STRUCTURE_DEVICE = ITEMS_REG.register("structure_gen_device", StructureGenSelectionDevice::new);
}
