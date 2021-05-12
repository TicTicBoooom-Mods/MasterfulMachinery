package com.ticticboooom.mods.mm.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.registration.MMPorts;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import lombok.Getter;
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

public class MachineProcessRecipe implements IRecipe<IInventory> {


    @Getter
    private final List<PortState> inputs;
    @Getter
    private final List<PortState> outputs;
    @Getter
    private int ticks;
    @Getter
    private String structureId;
    private ResourceLocation rl;

    public MachineProcessRecipe(List<PortState> inputs, List<PortState> outputs, int ticks, String structureId, ResourceLocation rl) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.ticks = ticks;
        this.structureId = structureId;
        this.rl = rl;
    }

    public boolean matches(List<IPortStorage> inputPorts, String structureId) {
        return structureId.equals(this.structureId) && canTake(inputPorts);
    }

    private boolean canTake(List<IPortStorage> inputPorts) {
        for (PortState input : inputs) {
            if (!input.validateRequirement(inputPorts)) {
                return false;
            }
        }
        return true;
    }

    private boolean canPut(List<IPortStorage> outputPorts) {
        for (PortState output : outputs) {
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
            for (PortState input : inputs) {
                input.processRequirement(inputPorts);
            }
            for (PortState output : outputs) {
                output.processResult(outputPorts);
            }
            update.setMsg("");
            update.setTicksTaken(0);
            return update;
        }

        boolean canTick = true;

        for (PortState input : inputs) {
            if (input.isConsumePerTick()) {
                if (!input.validateRequirement(inputPorts)) {
                    canTick = false;
                }
            }
        }
        for (PortState output : outputs) {
            if (output.isConsumePerTick()) {
                if (!output.validateResult(outputPorts)){
                    canTick = false;
                }
            }
        }

        if (canTick) {
            for (PortState input : inputs) {
                if (input.isConsumePerTick()) {
                    input.processRequirement(inputPorts);
                }
            }
            for (PortState input : outputs) {
                if (input.isConsumePerTick()) {
                    input.processResult(outputPorts);
                }
            }
            update.setTicksTaken(update.getTicksTaken() + 1);
        }
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
        return rl;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypes.PROCESS.get();
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

            List<PortState> inputStates = getStates(inputs);
            List<PortState> outputStates = getStates(outputs);

            return new MachineProcessRecipe(inputStates, outputStates, ticks, structureId, rl);
        }

        @SneakyThrows
        private List<PortState> getStates(JsonArray io) {
            List<PortState> ioStates = new ArrayList<>();
            for (JsonElement elem : io) {
                JsonObject out = elem.getAsJsonObject();
                String type = out.get("type").getAsString();
                boolean perTick = false;
                if (out.has("consumePerTick")) {
                    perTick = out.get("consumePerTick").getAsBoolean();
                }
                ResourceLocation typeRl = RLUtils.toRL(type);
                if (!MMPorts.PORTS.containsKey(typeRl)) {
                    throw new Exception(type + " is not a valid input type");
                }

                MasterfulPortType value = MMPorts.PORTS.get(typeRl);
                PortState data = value.getParser().createState(out.get("data").getAsJsonObject());
                data.setConsumePerTick(perTick);
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
            List<PortState> inputs = getStates(buf, inputCount);
            List<PortState> outputs = getStates(buf, outputCount);
            return new MachineProcessRecipe(inputs, outputs, ticks, structureId, rl);
        }

        private List<PortState> getStates(PacketBuffer buf, int count) {
            List<PortState> result = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String inpType = buf.readUtf();
                boolean perTick = buf.readBoolean();
                MasterfulPortType value = MMPorts.PORTS.get(RLUtils.toRL(inpType));
                PortState state = value.getParser().createState(buf);
                state.setConsumePerTick(perTick);
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

        private void writeStates(PacketBuffer buf, List<PortState> states) {
            for (PortState state : states) {
                MasterfulPortType value = MMPorts.PORTS.get(state.getName());
                buf.writeUtf(value.getRegistryName().toString());
                buf.writeBoolean(state.isConsumePerTick());
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
