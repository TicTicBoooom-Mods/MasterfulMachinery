package com.ticticboooom.mods.mm.ports.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@Getter
@AllArgsConstructor
public class MachineRecipeContext {
    private final List<PortStorage> inputs;
    private final List<PortStorage> outputs;
    private final ResourceLocation recipeId;
    private final String structureId;
    private final String controllerId;
    private final int ticks;
}