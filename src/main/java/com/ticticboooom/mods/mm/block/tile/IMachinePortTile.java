package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import net.minecraft.inventory.container.INamedContainerProvider;

public interface IMachinePortTile extends INamedContainerProvider {
    PortStorage getStorage();
    boolean isInput();
}
