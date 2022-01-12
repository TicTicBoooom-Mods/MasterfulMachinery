package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ref.MOD_ID);

    public static final RegistryObject<ControllerBlock> CONTROLLER = BLOCKS.register("controller", ControllerBlock::new);
}
