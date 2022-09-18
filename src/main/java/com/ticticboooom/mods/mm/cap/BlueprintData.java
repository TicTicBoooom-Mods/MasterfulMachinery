package com.ticticboooom.mods.mm.cap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlueprintData implements IBlueprintData {
    private ResourceLocation structure;



    @Override
    public ResourceLocation getStructure() {
        return structure;
    }

    @Override
    public void setStructure(ResourceLocation structure) {
        this.structure = structure;
    }

    public static final class Storage implements Capability.IStorage<IBlueprintData> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IBlueprintData> capability, IBlueprintData instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Structure", instance.getStructure() == null ? "" : instance.getStructure().toString());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IBlueprintData> capability, IBlueprintData instance, Direction side, INBT nbt) {
            CompoundNBT comp = (CompoundNBT) nbt;
            String structure = comp.getString("Structure");
            if (structure.equals("")){
                return;
            }
            instance.setStructure(ResourceLocation.tryCreate(structure));
        }
    }
}
