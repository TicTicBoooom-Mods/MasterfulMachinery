package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class PortState {
    @Getter
    @Setter
    private boolean consumePerTick = false;

    public abstract void processRequirement(List<IPortStorage> storage);
    public abstract boolean validateRequirement(List<IPortStorage> storage);
    public abstract void processResult(List<IPortStorage> storage);
    public abstract boolean validateResult(List<IPortStorage> storage);
    public abstract ResourceLocation getName();
}
