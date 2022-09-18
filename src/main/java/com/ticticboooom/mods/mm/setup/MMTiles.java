package com.ticticboooom.mods.mm.setup;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.ControllerTile;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.block.tile.ProjectorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MMTiles {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Ref.MOD_ID);

    public static final RegistryObject<TileEntityType<ControllerTile>> CONTROLLER = TILES.register("controller", () -> TileEntityType.Builder.create(ControllerTile::new, MMBlocks.CONTROLLER.get()).build(null));
    public static final RegistryObject<TileEntityType<PortTile>> PORT = TILES.register("port", () -> TileEntityType.Builder.create(PortTile::new, MMBlocks.PORT.get()).build(null));
    public static final RegistryObject<TileEntityType<ProjectorTile>> PROJECTOR = TILES.register("projector", () -> TileEntityType.Builder.create(ProjectorTile::new, MMBlocks.PROJECTOR.get()).build(null));

}
