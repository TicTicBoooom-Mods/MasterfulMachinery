package com.ticticboooom.mods.mm.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureBlockPos;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureObject;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureRecipeKeyModel;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureRecipeLegendModel;
import com.ticticboooom.mods.mm.exception.InvalidStructureDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.nbt.NBTActionParser;
import com.ticticboooom.mods.mm.nbt.NBTValidator;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class MachineStructureRecipe implements IRecipe<IInventory> {
    private final ResourceLocation rl;
    @Getter
    private String name;
    @Getter
    private List<List<MachineStructureRecipeKeyModel>> models;
    @Getter
    private final List<String> controllerId;
    private String id;

    public MachineStructureRecipe(List<MachineStructureRecipeKeyModel> models, List<String> controllerId, String id, ResourceLocation rl, String name) {
        this.rl = rl;
        this.name = name;
        List<MachineStructureRecipeKeyModel> rotated = new ArrayList<>();
        List<MachineStructureRecipeKeyModel> rotated1 = new ArrayList<>();
        List<MachineStructureRecipeKeyModel> rotated2 = new ArrayList<>();

        for (MachineStructureRecipeKeyModel model : models) {
            BlockPos rotatedPos = new BlockPos(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ()).rotate(Rotation.CLOCKWISE_90);
            BlockPos rotatedPos1 = new BlockPos(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ()).rotate(Rotation.CLOCKWISE_180);
            BlockPos rotatedPos2 = new BlockPos(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ()).rotate(Rotation.COUNTERCLOCKWISE_90);

            rotated.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ()), model.getTag(), model.getBlock(), model.getProperties()));
            rotated1.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos1.getX(), rotatedPos1.getY(), rotatedPos1.getZ()), model.getTag(), model.getBlock(), model.getProperties()));
            rotated2.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos2.getX(), rotatedPos2.getY(), rotatedPos2.getZ()), model.getTag(), model.getBlock(), model.getProperties()));
        }

        this.models = new ArrayList<>();
        this.models.add(models);
        this.models.add(rotated);
        this.models.add(rotated1);
        this.models.add(rotated2);
        this.controllerId = controllerId;
        this.id = id;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory p_77572_1_) {
        return null;
    }

    @Override
    public boolean canFit(int p_194133_1_, int p_194133_2_) {
        return false;
    }

    public int matches(BlockPos controllerPos, World world, String controllerId) {
        if (!this.controllerId.contains(controllerId)) {
            return -1;
        }

        int index = 0;
        for (List<MachineStructureRecipeKeyModel> model : models) {
            if (matchesWithRotation(controllerPos, world, model)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean matchesWithRotation(BlockPos controllerPos, World world, List<MachineStructureRecipeKeyModel> items) {
        for (MachineStructureRecipeKeyModel model : items) {
            if (!innerBlockMatch(controllerPos, world, model)) {
                return false;
            }
        }
        return true;
    }


    public boolean innerBlockMatch(BlockPos controllerPos, World world, MachineStructureRecipeKeyModel model) {
        BlockPos pos = controllerPos.add(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ());
        BlockState blockState = world.getBlockState(pos);
        boolean valid = false;
        if (!model.getTag().equals("")) {
            String[] split = model.getTag().split(":");
            if (split.length != 2) {
                MM.LOG.fatal("too many : (colons) in structure tag: {}", model.getTag());
                return false;
            }
            ITag<Block> tag = BlockTags.getCollection().get(new ResourceLocation(split[0], split[1]));
            if (tag == null) {
                MM.LOG.fatal("no existing block tag for structure tag: {}", model.getTag());
                return false;
            }
            valid = tag.contains(blockState.getBlock());
        } else if (!model.getBlock().equals("")) {
            valid = blockState.getBlock().getRegistryName().toString().equals(model.getBlock());
        }

        if (!valid) {
            return false;
        }

        if (model.getProperties() == null) {
            return true;
        }


        for (Map.Entry<String, String> stringStringEntry : model.getProperties().entrySet()) {
            for (Map.Entry<Property<?>, Comparable<?>> propertyEntry : blockState.getValues().entrySet()) {
                if (propertyEntry.getKey().getName().equals(stringStringEntry.getKey())) {
                    Optional<?> o = propertyEntry.getKey().parseValue(stringStringEntry.getValue());
                    return propertyEntry.getValue().equals(o.get());
                }
            }
        }

        return false;
    }

    public ArrayList<BlockPos> getPorts(BlockPos controllerPos, World world, int index) {
        ArrayList<BlockPos> result = new ArrayList<>();
        for (MachineStructureRecipeKeyModel model : models.get(index)) {
            BlockPos pos = controllerPos.add(model.getPos().toVector());
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof MachinePortBlock) {
                result.add(pos);
            }
        }
        return result;
    }

    public String getStructureId() {
        return id;
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
        return RecipeTypes.STRUCTURE.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypes.MACHINE_STRUCTURE;
    }

    public static final class Serializer implements IRecipeSerializer<MachineStructureRecipe> {


        @Override
        public MachineStructureRecipe read(ResourceLocation rl, JsonObject obj) {
            try {
                JsonElement controllerIdJson = obj.get("controllerId");
                List<String> ids = new ArrayList<>();
                if (controllerIdJson.isJsonPrimitive()) {
                    ids.add(controllerIdJson.getAsString());
                } else {
                    for (JsonElement jsonElement : controllerIdJson.getAsJsonArray()) {
                        ids.add(jsonElement.getAsString());
                    }
                }
                String id = obj.get("id").getAsString();
                String name = "";
                if (obj.has("name")) {
                    name = obj.get("name").getAsString();
                } else {
                    name = id;
                }
                DataResult<Pair<List<List<String>>, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(Codec.list(Codec.list(Codec.STRING))).apply(obj.getAsJsonArray("layout"));
                List<List<String>> layout = apply.result().get().getFirst();

                List<MachineStructureRecipeKeyModel> result = getResult(obj.getAsJsonObject("legend"), layout);


                validateStructure(result, ids, id, rl);
                MM.LOG.debug("Added structure '{}' with id '{}'", rl, id);
                return new MachineStructureRecipe(result, ids, id, rl, name);
            } catch (InvalidStructureDefinitionException e) {
                MM.LOG.error("InvalidStructureDefinition: " + e.getMessage());
            }
            return null;
        }

        private List<MachineStructureRecipeKeyModel> getResult(JsonObject legend, List<List<String>> layout) throws InvalidStructureDefinitionException {
            HashMap<Character, MachineStructureRecipeLegendModel> model = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : legend.entrySet()) {
                DataResult<Pair<MachineStructureRecipeLegendModel, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(MachineStructureRecipeLegendModel.CODEC).apply(entry.getValue());
                MachineStructureRecipeLegendModel first = apply.result().get().getFirst();
                model.put(entry.getKey().charAt(0), first);
            }
            ArrayList<MachineStructureRecipeKeyModel> result = new ArrayList<>();
            Vector3i controllerPos = getControllerPos(layout);

            int y = 0;
            int z = 0;
            for (List<String> layer : layout) {
                for (String row : layer) {
                    for (int x = 0; x < row.length(); x++) {
                        char c = row.charAt(x);
                        if (c == 'C') {
                            continue;
                        }
                        if (c == ' ') {
                            continue;
                        }
                        MachineStructureRecipeLegendModel machineStructureRecipeLegendModel = model.get(c);
                        BlockPos pos = new BlockPos(x, y, z).subtract(new BlockPos(controllerPos));
                        result.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(pos.getX(), pos.getY(), pos.getZ()), machineStructureRecipeLegendModel.getTag(), machineStructureRecipeLegendModel.getBlock(), machineStructureRecipeLegendModel.getProperties()));
                    }
                    z++;
                }
                y++;
                z = 0;
            }
            return result;
        }

        private Vector3i getControllerPos(List<List<String>> layout) throws InvalidStructureDefinitionException {
            int y = 0;
            int z = 0;
            for (List<String> layer : layout) {
                for (String row : layer) {
                    for (int x = 0; x < row.length(); x++) {
                        if (row.charAt(x) == 'C') {
                            return new Vector3i(x, y, z);
                        }
                    }
                    z++;
                }
                y++;
                z = 0;
            }
            throw new InvalidStructureDefinitionException("'C' AKA controller not found in layout section");
        }

        @SneakyThrows
        @Nullable
        @Override
        public MachineStructureRecipe read(ResourceLocation rl, PacketBuffer buf) {
            List<String> controllerId = new ArrayList<>();
            int idCount = buf.readInt();
            for (int i = 0; i < idCount; i++) {
                controllerId.add(buf.readString());
            }
            String id = buf.readString();
            String name = buf.readString();
            try {
                MachineStructureObject machineStructureObject = buf.func_240628_a_(MachineStructureObject.CODEC);
                List<MachineStructureRecipeKeyModel> models = machineStructureObject.getInner();
                return new MachineStructureRecipe(models, controllerId, id, rl, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void write(PacketBuffer buf, MachineStructureRecipe recipe) {
            buf.writeInt(recipe.controllerId.size());
            for (String s : recipe.controllerId) {
                buf.writeString(s);
            }
            buf.writeString(recipe.id);
            buf.writeString(recipe.name);
            try {
                buf.func_240629_a_(MachineStructureObject.CODEC, new MachineStructureObject(recipe.getModels().get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public IRecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return this;
        }

        @Override
        public ResourceLocation getRegistryName() {
            return new ResourceLocation(MM.ID, "machine_structure");
        }

        @Override
        public Class<IRecipeSerializer<?>> getRegistryType() {
            return RecipeTypes.STRUCTURE.get().getRegistryType();
        }

        private void validateStructure(List<MachineStructureRecipeKeyModel> models, List<String> controllerId, String id, ResourceLocation rl) throws InvalidStructureDefinitionException {
            for (MachineStructureRecipeKeyModel model : models) {
                if (!model.getBlock().equals("")) {
                    if (RLUtils.isRL(model.getBlock())) {
                        if (!ForgeRegistries.BLOCKS.containsKey(RLUtils.toRL(model.getBlock()))) {
                            throw new InvalidStructureDefinitionException("Block: " + model.getBlock() + " is not an existing block in the game");
                        }
                    } else {
                        throw new InvalidStructureDefinitionException("Block: " + model.getBlock() + " is defined but not a valid block id (ResourceLocation)");
                    }
                } else if (!model.getTag().equals("")) {
                    if (!RLUtils.isRL(model.getTag())) {
                        throw new InvalidStructureDefinitionException("Block Tag: " + model.getBlock() + " is defined but not a valid block tag id (ResourceLocation)");
                    }
                } else {
                    throw new InvalidStructureDefinitionException("YUo must define at least 1 'block' or 'tag' per port within the 'data' object");
                }
            }

            for (String s : controllerId) {
                boolean controllerIdFound = false;
                for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
                    controllerIdFound = block.get().getControllerId().equals(s) || controllerIdFound;
                }
                if (!controllerIdFound) {
                    throw new InvalidStructureDefinitionException("controllerId: " + s + " does not exist");
                }
            }
        }
    }
}
