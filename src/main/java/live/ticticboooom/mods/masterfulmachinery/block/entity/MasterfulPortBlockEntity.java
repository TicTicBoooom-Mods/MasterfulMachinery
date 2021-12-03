package live.ticticboooom.mods.masterfulmachinery.block.entity;

import live.ticticboooom.mods.masterfulmachinery.ports.base.MasterfulPortType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MasterfulPortBlockEntity extends BlockEntity {
    public MasterfulPortBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(MasterfulPortType.DEFAULT_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
}
