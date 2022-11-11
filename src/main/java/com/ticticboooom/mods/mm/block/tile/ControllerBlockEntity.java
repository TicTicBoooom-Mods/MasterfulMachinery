package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
import com.ticticboooom.mods.mm.model.ControllerDisplayData;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ControllerBlockEntity extends UpdatableTile implements ITickableTileEntity, INamedContainerProvider {


    private final RegistryObject<ContainerType<ControllerBlockContainer>> container;
    private final String controllerId;
    @Getter
    private ProcessUpdate processData = new ProcessUpdate();

    @Getter
    private ControllerDisplayData displayData = new ControllerDisplayData();

    public ControllerBlockEntity(RegistryObject<TileEntityType<?>> type, RegistryObject<ContainerType<ControllerBlockContainer>> container, String controllerId) {
        super(type.get());
        this.container = container;
        this.controllerId = controllerId;
    }

    @Override
    public void tick() {
        if (world.isRemote()) {
            return;
        }
        processData.setMsg("");
        boolean foundStructure = false;

        // Check if the controller has a structure attached to itself
        if (processData.getStructureDefinition().getStructure() != null) {
            MachineStructureRecipe structure = processData.getStructureDefinition().getStructure();
            int transformIndex = processData.getStructureDefinition().getTransformIndex();
            // Check if the structure is still matched in the world
            if (structure.matchesSpecificTransform(this.pos, world, transformIndex)) {
                processData.setStatus("Working");
                onStructureFound(structure, transformIndex);
                foundStructure = true;
            } else {
                // If the structure is no longer matched, invalidate the current recipe
                invalidateRecipe();
            }
        }

        // If the structure does not exist, attempt to find one that does
        List<MachineStructureRecipe> structures = world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_STRUCTURE);
        if (!foundStructure) {
            for (MachineStructureRecipe structure : structures) {
                int index = structure.matchesAnyTransform(this.pos, world, controllerId);
                if (index != -1) {
                    processData.getStructureDefinition().setStructure(structure);
                    processData.getStructureDefinition().setTransformIndex(index);
                    processData.setStatus("Working");
                    onStructureFound(structure, index);
                    foundStructure = true;
                    break;
                }
            }
        }

        // If no structure was found at all, invalidate the recipe.
        if (!foundStructure) {
            invalidateRecipe();
            processData.setStatus("Invalid Machine");
            processData.setTicksTaken(0);
            processData.getStructureDefinition().setStructure(null);
            processData.setRecipe(null);
        }
        update();
    }

    private void onStructureFound(MachineStructureRecipe structure, int index) {
        ArrayList<BlockPos> portPoses = structure.getPorts(pos, world, index);
        List<PortStorage> inputPorts = new ArrayList<>();
        List<PortStorage> outputPorts = new ArrayList<>();
        for (BlockPos pos : portPoses) {
            TileEntity blockEntity = world.getTileEntity(pos);
            if (blockEntity instanceof IMachinePortTile) {
                IMachinePortTile port = (IMachinePortTile) blockEntity;

                if (port.isInput()) {
                    inputPorts.add(port.getStorage());
                } else {
                    outputPorts.add(port.getStorage());
                }
            }
        }

        processData.getStructureDefinition().setInputPorts(inputPorts);
        processData.getStructureDefinition().setOutputPorts(outputPorts);
        onPortsEstablished(inputPorts, outputPorts, structure);
    }

    private void onPortsEstablished(List<PortStorage> inputPorts, List<PortStorage> outputPorts, MachineStructureRecipe structure) {
        List<MachineProcessRecipe> processRecipes = world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_PROCESS);
        boolean processed = false;
        // Maybe instead of checking all recipe again first check if our current recipe is still valid?
        if (processData.getRecipe() != null && processData.getRecipe().matches(inputPorts, structure.getStructureId(), processData)) {
            processData.getRecipe().process(inputPorts, outputPorts, processData);
            return;
        }

        // If we haven't processed the previous recipe that means it needs to be invalidated
        for (MachineProcessRecipe recipe : processRecipes) {
            if (recipe.matches(inputPorts, structure.getStructureId(), processData)) {
                // TODO Make sure the recipe doesn't stop progress when some inputs aren't present
                if (!recipe.equals(processData.getRecipe())) {
                    if (processData.getRecipe() != null) {
                        processData.getRecipe().onInterrupted(inputPorts, outputPorts);
                    }
                    //processData.setTicksTaken(0);
                }
                processData.setRecipe(recipe);
                recipe.process(inputPorts, outputPorts, processData);
                processed = true;
                break;
            }

        }

        if (!processed) {
            invalidateRecipe();
        }
    }

    public void invalidateRecipe() {
        processData.setStatus("Idle");
        if (processData.getStructureDefinition().getStructure() != null && processData.getRecipe() != null) {
            processData.getRecipe().onInterrupted(processData.getStructureDefinition().getInputPorts(), processData.getStructureDefinition().getOutputPorts());
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.masterfulmachinery." + this.controllerId + "_controller.name");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ControllerBlockContainer(container.get(), p_createMenu_1_, p_createMenu_2_, this);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        this.displayData.fromProcess(this.processData);
        nbt.putInt("ticks", processData.getTicksTaken());
        nbt.putString("msg", processData.getMsg());
        nbt.put("DisplayData", this.displayData.serialize());
        return super.write(nbt);
    }

    @Override
    public void read(BlockState p_230337_1_, CompoundNBT nbt) {
        super.read(p_230337_1_, nbt);
        processData.setTicksTaken(nbt.getInt("ticks"));
        processData.setMsg(nbt.getString("msg"));
        this.displayData.deserialize(nbt.getCompound("DisplayData"));
    }
}
