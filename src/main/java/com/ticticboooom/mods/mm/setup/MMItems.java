package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.item.ControllerBlockItem;
import com.ticticboooom.mods.mm.block.item.PortBlockItem;
import com.ticticboooom.mods.mm.item.BlueprintItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Ref.MOD_ID);

    public static final RegistryObject<ControllerBlockItem> CONTROLLER = ITEMS.register("controller", ControllerBlockItem::new);
    public static final RegistryObject<PortBlockItem> PORT = ITEMS.register("port", PortBlockItem::new);
    public static final RegistryObject<BlueprintItem> BLUEPRINT = ITEMS.register("blueprint", BlueprintItem::new);
}
