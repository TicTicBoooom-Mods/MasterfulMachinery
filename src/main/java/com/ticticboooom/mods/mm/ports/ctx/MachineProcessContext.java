package com.ticticboooom.mods.mm.ports.ctx;

import com.ticticboooom.mods.mm.data.ProcessModel;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class MachineProcessContext {
    public ProcessModel processModel;
    public List<PortStorage> inputs;
    public List<PortStorage> outputs;
    public ResourceLocation processId;
    public ResourceLocation structureId;
    public int ticks;
    public int maxTicks;
    public MachineStructureContext structureCtx;
}