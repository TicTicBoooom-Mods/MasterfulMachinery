package com.ticticboooom.mods.mm.model;

import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WorldStructureDefinition {
    private int transformIndex;
    private MachineStructureRecipe structure;
    private List<PortStorage> inputPorts;
    private List<PortStorage> outputPorts;
}
