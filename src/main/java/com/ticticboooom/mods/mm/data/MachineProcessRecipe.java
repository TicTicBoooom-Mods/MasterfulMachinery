package com.ticticboooom.mods.mm.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.ports.state.IPortState;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.registration.MMPorts;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import lombok.SneakyThrows;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// TODO: processing io

public class MachineProcessRecipe implements IRecipe<IInventory> {


    private final List<IPortState> inputs;
    private final List<IPortState> outputs;
    private int ticks;
    private String structureId;

    public MachineProcessRecipe(List<IPortState> inputs, List<IPortState> outputs, int ticks, String structureId) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.ticks = ticks;
        this.structureId = structureId;
    }

    public boolean matches(List<IPortStorage> inputPorts, String structureId) {
        return structureId.equals(this.structureId) && canTake(inputPorts);
    }

    private boolean canTake(List<IPortStorage> inputPorts) {
        for (IPortState input : inputs) {
            if (!input.validateRequirement(inputPorts)) {
                return false;
            }
        }
        return true;
    }

    private boolean canPut(List<IPortStorage> outputPorts) {
        for (IPortState output : outputs) {
            if (!output.validateResult(outputPorts)) {
                return false;
            }
        }
        return true;
    }

    public ProcessUpdate process(List<IPortStorage> inputPorts, List<IPortStorage> outputPorts, ProcessUpdate update) {
        boolean canTake = canTake(inputPorts);
        boolean canPut = canPut(outputPorts);

        if (!canTake || !canPut) {
            update.setMsg("Not enough space \nin output ports");
            return update;
        }

        if (update.getTicksTaken() >= ticks) {
            for (IPortState output : inputs) {
                output.processRequirement(inputPorts);
            }
            for (IPortState output : outputs) {
                output.processResult(outputPorts);
            }
            update.setMsg("");
            update.setTicksTaken(0);
            return update;
        }
        update.setTicksTaken(update.getTicksTaken() + 1);
        update.setMsg((int)(((float)update.getTicksTaken() /(float)ticks) * 100) + "%");
        return update;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory p_77572_1_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(MM.ID, "machine_process");
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypes.MACHINE_PROCESS;
    }

    public static final class Serializer implements IRecipeSerializer<MachineProcessRecipe> {

        @SneakyThrows
        @Override
        public MachineProcessRecipe fromJson(ResourceLocation rl, JsonObject obj) {
            int ticks = obj.get("ticks").getAsInt();
            String structureId = obj.get("structureId").getAsString();
            JsonArray inputs = obj.get("inputs").getAsJsonArray();
            JsonArray outputs = obj.get("outputs").getAsJsonArray();

            List<IPortState> inputStates = getStates(inputs);
            List<IPortState> outputStates = getStates(outputs);

            return new MachineProcessRecipe(inputStates, outputStates, ticks, structureId);
        }

        @SneakyThrows
        private List<IPortState> getStates(JsonArray io) {
            List<IPortState> ioStates = new ArrayList<>();
            for (JsonElement elem : io) {
                JsonObject out = elem.getAsJsonObject();
                String type = out.get("type").getAsString();
                ResourceLocation typeRl = RLUtils.toRL(type);
                if (!MMPorts.PORTS.containsKey(typeRl)) {
                    throw new Exception(type + " is not a valid input type");
                }

                MasterfulPortType value = MMPorts.PORTS.get(typeRl);
                IPortState data = value.getParser().createState(out.get("data").getAsJsonObject());
                ioStates.add(data);
            }
            return ioStates;
        }


        @Nullable
        @Override
        public MachineProcessRecipe fromNetwork(ResourceLocation rl, PacketBuffer buf) {
            int inputCount = buf.readInt();
            int outputCount = buf.readInt();
            int ticks = buf.readInt();
            String structureId = buf.readUtf();
            List<IPortState> inputs = getStates(buf, inputCount);
            List<IPortState> outputs = getStates(buf, outputCount);
            return new MachineProcessRecipe(inputs, outputs, ticks, structureId);
        }

        private List<IPortState> getStates(PacketBuffer buf, int count) {
            List<IPortState> result = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String inpType = buf.readUtf();
                MasterfulPortType value = MMPorts.PORTS.get(RLUtils.toRL(inpType));
                IPortState state = value.getParser().createState(buf);
                result.add(state);
            }
            return result;
        }

        @Override
        public void toNetwork(PacketBuffer buf, MachineProcessRecipe recipe) {
            buf.writeInt(recipe.inputs.size());
            buf.writeInt(recipe.outputs.size());
            buf.writeInt(recipe.ticks);
            buf.writeUtf(recipe.structureId);

            writeStates(buf, recipe.inputs);
            writeStates(buf, recipe.outputs);
        }

        private void writeStates(PacketBuffer buf, List<IPortState> states) {
            for (IPortState state : states) {
                MasterfulPortType value = MMPorts.PORTS.get(state.getName());
                buf.writeUtf(value.getRegistryName().toString());
                value.getParser().write(buf, state);
            }
        }

        @Override
        public IRecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return this;
        }

        @Override
        public ResourceLocation getRegistryName() {
            return new ResourceLocation(MM.ID, "machine_process");
        }

        @Override
        public Class<IRecipeSerializer<?>> getRegistryType() {
            return RecipeTypes.PROCESS.get().getRegistryType();
        }
    }
}
