package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.container.ControllerBlockContainer;
import com.ticticboooom.mods.mm.data.MachineProcessRecipe;
import com.ticticboooom.mods.mm.data.MachineStructureRecipe;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ControllerBlockEntity extends UpdatableTile implements ITickableTileEntity, INamedContainerProvider {


    private RegistryObject<ContainerType<ControllerBlockContainer>> container;
    private String controllerId;
    @Getter
    private ProcessUpdate update = new ProcessUpdate(0, "", "", "");

    public ControllerBlockEntity(RegistryObject<TileEntityType<?>> type, RegistryObject<ContainerType<ControllerBlockContainer>> container, String controllerId) {
        super(type.get());
        this.container = container;
        this.controllerId = controllerId;
    }

    @Override
    public void tick() {
        if (world.isRemote()){
            return;
        }
        update.setMsg("Failed to construct \nthe machine");
        List<MachineStructureRecipe> recipes = world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_STRUCTURE);
        for (MachineStructureRecipe recipe : recipes) {
            int index = recipe.matches(this.pos, world, controllerId);
            if (index != -1) {
                if (!update.getSid().equals(recipe.getId().toString())){
                    update.setTicksTaken(0);
                }
                update.setSid(recipe.getId().toString());
                update.setMsg("Found structure");
                onStructureFound(recipe, index);
                break;
            }
        }
        update();
    }

    private void onStructureFound(MachineStructureRecipe structure, int index) {
        ArrayList<BlockPos> ports = structure.getPorts(pos, world, index);
        List<PortStorage> inputPorts = new ArrayList<>();
        List<PortStorage> outputPorts = new ArrayList<>();
        for (BlockPos port : ports) {
            TileEntity blockEntity = world.getTileEntity(port);
            if (blockEntity instanceof MachinePortBlockEntity) {
                MachinePortBlockEntity portBE = (MachinePortBlockEntity) blockEntity;

                if (portBE.isInput()) {
                    inputPorts.add(portBE.getStorage());
                } else {
                    outputPorts.add(portBE.getStorage());
                }
            }
        }

        onPortsEstablished(inputPorts, outputPorts, structure);
    }

    private void onPortsEstablished(List<PortStorage> inputPorts, List<PortStorage> outputPorts, MachineStructureRecipe structure) {
        List<MachineProcessRecipe> processRecipes = world.getRecipeManager().getRecipesForType(RecipeTypes.MACHINE_PROCESS);
        boolean processed = false;
        for (MachineProcessRecipe recipe : processRecipes) {
            if (recipe.matches(inputPorts, structure.getStructureId())) {
                if (!update.getId().equals(recipe.getId().toString())){
                    update.setTicksTaken(0);
                }
                this.update.setId(recipe.getId().toString());
                this.update = recipe.process(inputPorts, outputPorts, this.update);
                processed = true;
                break;
            }
        }
        if (!processed) {
            this.update.setTicksTaken(0);
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
        nbt.putInt("ticks", update.getTicksTaken());
        nbt.putString("msg", update.getMsg());
        return super.write(nbt);
    }

    @Override
    public void read(BlockState p_230337_1_, CompoundNBT nbt) {
        super.read(p_230337_1_, nbt);
        update.setTicksTaken(nbt.getInt("ticks"));
        update.setMsg(nbt.getString("msg"));
    }
}
