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
import com.ticticboooom.mods.mm.exception.InvalidStructureDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineStructureRecipe implements IRecipe<IInventory> {
    private final ResourceLocation rl;
    @Getter
    private List<List<MachineStructureRecipeKeyModel>> models;
    @Getter
    private final List<String> controllerId;
    private String id;

    public MachineStructureRecipe(List<MachineStructureRecipeKeyModel> models, List<String> controllerId, String id, ResourceLocation rl)  {
        this.rl = rl;
        List<MachineStructureRecipeKeyModel> rotated = new ArrayList<>();
        List<MachineStructureRecipeKeyModel> rotated1 = new ArrayList<>();
        List<MachineStructureRecipeKeyModel> rotated2 = new ArrayList<>();

        for (MachineStructureRecipeKeyModel model : models) {
            BlockPos rotatedPos = new BlockPos(model.getPos().getZ(), model.getPos().getY(), model.getPos().getX());
            rotated.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos.getX(), rotatedPos.getY(), rotatedPos.getZ()), model.getTag(),model.getBlock(), model.getNbt()));

            BlockPos rotatedPos1 = new BlockPos(-model.getPos().getX(), model.getPos().getY(), -model.getPos().getZ());
            rotated1.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos1.getX(), rotatedPos1.getY(), rotatedPos1.getZ()), model.getTag(),model.getBlock(), model.getNbt()));

            BlockPos rotatedPos2 = new BlockPos(-model.getPos().getZ(), model.getPos().getY(), -model.getPos().getX());
            rotated2.add(new MachineStructureRecipeKeyModel(new MachineStructureBlockPos(rotatedPos2.getX(), rotatedPos2.getY(), rotatedPos2.getZ()), model.getTag(),model.getBlock(), model.getNbt()));
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
    public ItemStack assemble(IInventory p_77572_1_) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
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

    private boolean matchesWithRotation(BlockPos controllerPos, World world, List<MachineStructureRecipeKeyModel> items) {
        for (MachineStructureRecipeKeyModel model : items) {
            if (!innerBlockMatch(controllerPos, world, model)) {
                return false;
            }
        }
        return true;
    }


    private boolean innerBlockMatch(BlockPos controllerPos, World world, MachineStructureRecipeKeyModel model) {
        BlockPos pos = controllerPos.offset(model.getPos().getX(), model.getPos().getY(), model.getPos().getZ());
        BlockState blockState = world.getBlockState(pos);
        boolean valid = false;
        if (!model.getTag().equals("")) {
            String[] split = model.getTag().split(":");
            if (split.length != 2){
                MM.LOG.fatal("too many : (colons) in structure tag: {}", model.getTag());
                return false;
            }
            ITag<Block> tag = BlockTags.getAllTags().getTag(new ResourceLocation(split[0], split[1]));
            if (tag == null) {
                MM.LOG.fatal("no existing block tag for structure tag: {}", model.getTag());
                return false;
            }
            valid = tag.contains(blockState.getBlock());
        } else if (!model.getBlock().equals("")){
            valid = blockState.getBlock().getRegistryName().toString().equals(model.getBlock());
        }

        if (!valid) {
            return false;
        }

        if (model.getNbt().equals("")) {
            return true;
        }

        if (!blockState.hasTileEntity()){
            return true;
        }
        try {
            CompoundNBT compoundNBT = JsonToNBT.parseTag(model.getNbt());
            TileEntity blockEntity = world.getBlockEntity(pos);

            return compoundNBT.equals(blockEntity.getTileData());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<BlockPos> getPorts(BlockPos controllerPos, World world, int index) {
        ArrayList<BlockPos> result = new ArrayList<>();
        for (MachineStructureRecipeKeyModel model : models.get(index)) {
            BlockPos pos = controllerPos.offset(model.getPos().toVector());
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
    public ItemStack getResultItem() {
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
        public MachineStructureRecipe fromJson(ResourceLocation rl, JsonObject obj) {
            JsonElement controllerIdJson = obj.get("controllerId");
            List<String> ids = new ArrayList<>();
            if (controllerIdJson.isJsonPrimitive()){
                ids.add(controllerIdJson.getAsString());
            } else {
                for (JsonElement jsonElement : controllerIdJson.getAsJsonArray()) {
                    ids.add(jsonElement.getAsString());
                }
            }
            String id = obj.get("id").getAsString();
            DataResult<Pair<List<MachineStructureRecipeKeyModel>, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(Codec.list(MachineStructureRecipeKeyModel.CODEC)).apply(obj.getAsJsonArray("blocks"));
            List<MachineStructureRecipeKeyModel> first = apply.result().get().getFirst();
            validateStructure(first, ids, id, rl);
            return new MachineStructureRecipe(first, ids, id, rl);
        }

        @SneakyThrows
        @Nullable
        @Override
        public MachineStructureRecipe fromNetwork(ResourceLocation rl, PacketBuffer buf) {
            List<String> controllerId = new ArrayList<>();
            int idCount = buf.readInt();
            for (int i = 0; i < idCount; i++) {
                controllerId.add(buf.readUtf());
            }
            String id = buf.readUtf();
            try {
                MachineStructureObject machineStructureObject = buf.readWithCodec(MachineStructureObject.CODEC);
                List<MachineStructureRecipeKeyModel> models = machineStructureObject.getInner();
                return new MachineStructureRecipe(models, controllerId, id, rl);
            } catch (Exception  e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void toNetwork(PacketBuffer buf, MachineStructureRecipe recipe) {
            buf.writeInt(recipe.controllerId.size());
            for (String s : recipe.controllerId) {
                buf.writeUtf(s);
            }
            buf.writeUtf(recipe.id);
            try {
                buf.writeWithCodec(MachineStructureObject.CODEC, new MachineStructureObject(recipe.getModels().get(0)));
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

        @SneakyThrows
        private void validateStructure(List<MachineStructureRecipeKeyModel> models, List<String> controllerId, String id, ResourceLocation rl) {
            for (MachineStructureRecipeKeyModel model : models) {
                if (!model.getBlock().equals("")) {
                    if (RLUtils.isRL(model.getBlock())) {
                        if (!ForgeRegistries.ITEMS.containsKey(RLUtils.toRL(model.getBlock()))) {
                            throw new InvalidStructureDefinitionException("Block: " + model.getBlock() +  " is not an existing block in the game");
                        }
                    } else {
                        throw new InvalidStructureDefinitionException("Block: " + model.getBlock() +  " is defined but not a valid block id (ResourceLocation)");
                    }
                } else if (!model.getTag().equals("")){
                    if (RLUtils.isRL(model.getTag())) {
                        if (!BlockTags.getAllTags().getAllTags().containsKey(RLUtils.toRL(model.getTag()))) {
                            throw new InvalidStructureDefinitionException("Block Tag: " + model.getBlock() +  " is not an existing block tag in the game");
                        }
                    } else {
                        throw new InvalidStructureDefinitionException("Block Tag: " + model.getBlock() +  " is defined but not a valid block tag id (ResourceLocation)");
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
                if (!controllerIdFound){
                    throw new InvalidStructureDefinitionException("controllerId: " + s + " does not exist");
                }
            }
        }

    }
}
