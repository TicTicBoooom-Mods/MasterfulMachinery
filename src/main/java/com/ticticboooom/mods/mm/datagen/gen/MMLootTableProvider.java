package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class MMLootTableProvider extends BaseLootTableProvider {
    public MMLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        for (RegistryObject<ControllerBlock> controller : MMLoader.BLOCKS) {
            if (!controller.isPresent()){
                return;
            }
            blockLootTable.put(controller.get(), createItemLootTable(ForgeRegistries.ITEMS.getValue(controller.getId())));
        }

        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            blockLootTable.put(port.get(), createItemLootTable(ForgeRegistries.ITEMS.getValue(port.getId())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            blockLootTable.put(port.get(), createItemLootTable(ForgeRegistries.ITEMS.getValue(port.getId())));
        }

        blockLootTable.put(MMSetup.PROJECTOR_BLOCK.get(), createCountTable(MMSetup.PROJECTOR_ITEM.get(), MMSetup.PROJECTOR_ITEM.get(), 1, 1));
        blockLootTable.put(MMSetup.STRUCTURE_BLOCK.get(), createBlockLootTable(MMSetup.STRUCTURE_BLOCK.get()));

    }
}
