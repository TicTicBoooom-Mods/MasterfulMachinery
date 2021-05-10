package com.ticticboooom.mods.mm.registration;

import com.ticticboooom.mods.mm.MM;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMSetup {

    public static final DeferredRegister<Block> BLOCKS_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, MM.ID);
    public static final DeferredRegister<Item> ITEMS_REG = DeferredRegister.create(ForgeRegistries.ITEMS, MM.ID);
    public static final DeferredRegister<TileEntityType<?>> TILES_REG = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MM.ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINER_REG = DeferredRegister.create(ForgeRegistries.CONTAINERS, MM.ID);

    public static final RegistryObject<Item> BLUEPRINT = ITEMS_REG.register("blueprint", () -> new Item(new Item.Properties().tab(MMLoader.MASTERFUL_ITEM_GROUP)));

}
