package com.ticticboooom.mods.mm.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.exception.InvalidProcessDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.model.ProcessUpdate;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.ports.state.PortState;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
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
import java.util.Random;

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

    private List<Double> inputRolls = new ArrayList<>();
    private List<Double> outputRolls = new ArrayList<>();
    private Random rand = new Random();

    public MachineProcessRecipe(List<PortState> inputs, List<PortState> outputs, int ticks, String structureId, ResourceLocation rl) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.ticks = ticks;
        this.structureId = structureId;
        this.rl = rl;
    }

    public boolean matches(List<PortStorage> inputPorts, String structureId, ProcessUpdate update) {
        return structureId.equals(this.structureId) && canTake(inputPorts, update.getTakenIndices());
    }

    private boolean canTake(List<PortStorage> inputPorts, List<Integer> takenIndices) {
        int i = -1;
        for (PortState input : inputs) {
            i++;
            if (takenIndices.contains(i)) {
                continue;
            }
            if (!input.isConsumePerTick()) {
                if (!input.validateRequirement(inputPorts)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canPut(List<PortStorage> outputPorts) {
        for (PortState output : outputs) {
            if (!output.validateResult(outputPorts)) {
                return false;
            }
        }
        return true;
    }

    private void resetChances() {
        inputRolls.clear();
        outputRolls.clear();
        for (PortState input : inputs) {
            if (input.supportsPerTick()) {
                inputRolls.add(rand.nextDouble());
            } else {
                inputRolls.add(1.0);
            }
        }
        for (PortState output : outputs) {
            if (output.supportsPerTick()) {
                outputRolls.add(rand.nextDouble());
            } else {
                outputRolls.add(1.0);
            }
        }
    }

    public ProcessUpdate process(List<PortStorage> inputPorts, List<PortStorage> outputPorts, ProcessUpdate update) {
        resetChances();
        boolean canTake = canTake(inputPorts, update.getTakenIndices());
        boolean canPut = canPut(outputPorts);

        if (!canTake || !canPut) {
            update.setMsg("Found Structure");
            return update;
        }

        int takenIndex = 0;
        if (update.getTicksTaken() <= 0) {
            for (PortState input : inputs) {
                if (input.isInstantConsume() && input.validateRequirement(inputPorts)) {
                    update.getTakenIndices().add(takenIndex);
                    input.processRequirement(inputPorts);
                }
                takenIndex++;
            }
        }

        int index = 0;
        if (update.getTicksTaken() >= ticks) {
            update.getTakenIndices().clear();
            for (PortState input : inputs) {
                if (input.isConsumePerTick() || input.isInstantConsume()) {
                    continue;
                }
                if (inputRolls.get(index) < input.getChance()) {
                    input.processRequirement(inputPorts);
                    index++;
                }
            }
            index = 0;
            for (PortState output : outputs) {
                if (output.isConsumePerTick()) {
                    continue;
                }
                if (outputRolls.get(index) < output.getChance()) {
                    output.processResult(outputPorts);
                }
            }
            update.setMsg("");
            update.setTicksTaken(0);
            update.setTakenIndices(new ArrayList<>());
            return update;
        }

        boolean canTick = true;


        index = 0;
        for (PortState input : inputs) {
            if (input.isConsumePerTick()) {
                if (inputRolls.get(index) < input.getChance()) {
                    if (!input.validateRequirement(inputPorts)) {
                        canTick = false;
                    }
                }
            }
        }
        index = 0;
        for (PortState output : outputs) {
            if (outputRolls.get(index) < output.getChance()) {
                if (output.isConsumePerTick()) {
                    if (!output.validateResult(outputPorts)) {
                        canTick = false;
                    }
                }
            }
        }

        if (canTick) {
            for (PortState input : inputs) {
                if (inputRolls.get(index) < input.getChance()) {
                    if (input.isConsumePerTick()) {
                        input.processRequirement(inputPorts);
                    }
                }
            }
            for (PortState output : outputs) {
                if (outputRolls.get(index) < output.getChance()) {
                    if (output.isConsumePerTick()) {
                        output.processResult(outputPorts);
                    }
                }
            }
            update.setTicksTaken(update.getTicksTaken() + 1);
        }
        update.setMsg((int) (((float) update.getTicksTaken() / (float) ticks) * 100) + "%");
        return update;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
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

        @Override
        public MachineProcessRecipe read(ResourceLocation rl, JsonObject obj) {
            try {
                int ticks = obj.get("ticks").getAsInt();
                String structureId = obj.get("structureId").getAsString();
                JsonArray inputs = obj.get("inputs").getAsJsonArray();
                JsonArray outputs = obj.get("outputs").getAsJsonArray();

                List<PortState> inputStates = getStates(inputs);
                List<PortState> outputStates = getStates(outputs);
                validateProcess(inputStates, outputStates, ticks, structureId, rl);
                MM.LOG.debug("Added process '{}' for structure '{}'", rl, structureId);
                return new MachineProcessRecipe(inputStates, outputStates, ticks, structureId, rl);
            } catch (InvalidProcessDefinitionException e) {
                MM.LOG.error("InvalidProcessDefinition: " + e.getMessage());
            }
            return null;
        }

        @SneakyThrows
        private List<PortState> getStates(JsonArray io) {
            List<PortState> ioStates = new ArrayList<>();
            for (JsonElement elem : io) {
                JsonObject out = elem.getAsJsonObject();
                String type = out.get("type").getAsString();
                boolean perTick = false;
                if (out.has("perTick")) {
                    perTick = out.get("perTick").getAsBoolean();
                } else if (out.has("consumePerTick")) {
                    perTick = out.get("consumePerTick").getAsBoolean();
                }

                ResourceLocation typeRl = RLUtils.toRL(type);
                if (!MMPorts.PORTS.containsKey(typeRl)) {
                    throw new Exception(type + " is not a valid input type");
                }

                double chance = 1;
                if (out.has("chance")) {
                    chance = out.get("chance").getAsDouble();
                }

                boolean consumeInstantly = false;
                if (out.has("consumeInstantly")) {
                    consumeInstantly = out.get("consumeInstantly").getAsBoolean();
                }

                MasterfulPortType value = MMPorts.PORTS.get(typeRl);
                PortState data = value.getParser().createState(out.get("data").getAsJsonObject());
                data.setConsumePerTick(perTick);
                data.setChance(chance);
                data.setInstantConsume(consumeInstantly);
                ioStates.add(data);
            }
            return ioStates;
        }


        @Nullable
        @Override
        public MachineProcessRecipe read(ResourceLocation rl, PacketBuffer buf) {
            int inputCount = buf.readInt();
            int outputCount = buf.readInt();
            int ticks = buf.readInt();
            String structureId = buf.readString();
            List<PortState> inputs = getStates(buf, inputCount);
            List<PortState> outputs = getStates(buf, outputCount);
            return new MachineProcessRecipe(inputs, outputs, ticks, structureId, rl);
        }

        private List<PortState> getStates(PacketBuffer buf, int count) {
            List<PortState> result = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String inpType = buf.readString();
                boolean perTick = buf.readBoolean();
                double chance = buf.readDouble();
                MasterfulPortType value = MMPorts.PORTS.get(RLUtils.toRL(inpType));
                PortState state = value.getParser().createState(buf);
                state.setConsumePerTick(perTick);
                state.setChance(chance);
                result.add(state);
            }
            return result;
        }

        @Override
        public void write(PacketBuffer buf, MachineProcessRecipe recipe) {
            buf.writeInt(recipe.inputs.size());
            buf.writeInt(recipe.outputs.size());
            buf.writeInt(recipe.ticks);
            buf.writeString(recipe.structureId);

            writeStates(buf, recipe.inputs);
            writeStates(buf, recipe.outputs);
        }

        private void writeStates(PacketBuffer buf, List<PortState> states) {
            for (PortState state : states) {
                MasterfulPortType value = MMPorts.PORTS.get(state.getName());
                buf.writeString(value.getRegistryName().toString());
                buf.writeBoolean(state.isConsumePerTick());
                buf.writeDouble(state.getChance());
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


        private void validateProcess(List<PortState> inputs, List<PortState> outputs, int ticks, String structureId, ResourceLocation rl) throws InvalidProcessDefinitionException {
            for (PortState input : inputs) {
                input.validateDefinition();
                commonValidate(input);
            }

            for (PortState output : outputs) {
                output.validateDefinition();
                commonValidate(output);
            }
        }

        private void commonValidate(PortState state) throws InvalidProcessDefinitionException {
            if (!state.supportsChances() && state.getChance() != 1) {
                throw new InvalidProcessDefinitionException("Port Type: " + state.getName() + " does not support chanced operations (chance)");
            }
            if (state.getChance() < 0 || state.getChance() > 1) {
                throw new InvalidProcessDefinitionException("Ingredient chance must be between 0 and 1");
            }
            if (!state.supportsPerTick() && state.isConsumePerTick()) {
                throw new InvalidProcessDefinitionException("Port Type: " + state.getName() + " does not support per tick operations (perTick)");
            }
        }
    }
}
