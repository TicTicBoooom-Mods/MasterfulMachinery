package com.ticticboooom.mods.mm.model;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundNBT;

@Setter
@Getter
public class ControllerDisplayData {

    private String structureName;
    private String recipeName;
    private String status;
    private String progress;
    private String message;

    public void fromProcess(ProcessUpdate process) {
        this.structureName = "";
        if (process.getStructureDefinition().getStructure() != null) {
            this.structureName = process.getStructureDefinition().getStructure().getName();
        }

        this.recipeName = "";
        if (process.getRecipe() != null && process.getRecipe().hasName()) {
            this.recipeName = process.getRecipe().getName();
        }

        this.status = process.getStatus();
        this.progress = "";
        if (process.getRecipe() != null) {
            float percentage = ((float) process.getTicksTaken() / (float) process.getRecipe().getTicks()) * 100;
            this.progress = String.format("%d/%d (%.2f%%)", process.getTicksTaken(), process.getRecipe().getTicks(), percentage);
        }
        this.message = process.getMsg();
    }

    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("StructureName", this.structureName);
        nbt.putString("RecipeName", this.recipeName);
        nbt.putString("Status", this.status);
        nbt.putString("Progress", this.progress);
        nbt.putString("Message", this.message);
        return nbt;
    }

    public void deserialize(CompoundNBT nbt) {
        this.structureName = nbt.getString("StructureName");
        this.recipeName = nbt.getString("RecipeName");
        this.status = nbt.getString("Status");
        this.progress = nbt.getString("Progress");
        this.message = nbt.getString("Message");
    }
}
