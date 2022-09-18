package com.ticticboooom.mods.mm.block.item;

import com.ticticboooom.mods.mm.setup.MMBlocks;
import net.minecraft.item.BlockItem;

public class ProjectorItem extends BlockItem {

    public ProjectorItem() {
        super(MMBlocks.PROJECTOR.get(), new Properties().group(MMItemGroup.INSTANCE));
    }
}
