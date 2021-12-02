package live.ticticboooom.mods.masterfulmachinery.ports.base;

import live.ticticboooom.mods.masterfulmachinery.block.MasterfulPortBlock;
import live.ticticboooom.mods.masterfulmachinery.block.entity.MasterfulPortBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class MasterfulPortType extends ForgeRegistryEntry<MasterfulPortType> {
    public static final Lazy<MasterfulPortBlock> DEFAULT_BLOCK = Lazy.of(MasterfulPortBlock::new);
    public static final Lazy<BlockEntityType<MasterfulPortBlockEntity>> DEFAULT_BLOCK_ENTITY = Lazy.of(() -> BlockEntityType.Builder.of(MasterfulPortBlockEntity::new, DEFAULT_BLOCK.get()).build(null));

    public BlockEntityType<? extends BlockEntity> blockEntityType() {
        return DEFAULT_BLOCK_ENTITY.get();
    }

    public Block block() {
        return DEFAULT_BLOCK.get();
    }

}
