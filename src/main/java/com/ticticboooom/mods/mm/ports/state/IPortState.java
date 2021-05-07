package com.ticticboooom.mods.mm.ports.state;

import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IPortState {
    void processRequirement(List<IPortStorage> storage);
    boolean validateRequirement(List<IPortStorage> storage);
    void processResult(List<IPortStorage> storage);
    boolean validateResult(List<IPortStorage> storage);
    ResourceLocation getName();
}
