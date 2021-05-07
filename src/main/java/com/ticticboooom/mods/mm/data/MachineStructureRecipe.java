package com.ticticboooom.mods.mm.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.data.model.structure.MachineStructureRecipeKeyModel;
import com.ticticboooom.mods.mm.registration.RecipeTypes;
import lombok.Getter;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MachineStructureRecipe implements IRecipe<IInventory> {
    private final ResourceLocation rl = new ResourceLocation(MM.ID, "machine_structure");
    private List<MachineStructureRecipeKeyModel> models;
    private final String controllerId;
    private String id;

    public MachineStructureRecipe(List<MachineStructureRecipeKeyModel> models, String controllerId, String id)  {
        this.models = models;
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

    public boolean matches(BlockPos controllerPos, World world, String controllerId) {
        if (!this.controllerId.equals(controllerId)) {
            return false;
        }

        for (MachineStructureRecipeKeyModel model : models) {
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

    public ArrayList<BlockPos> getPorts(BlockPos controllerPos, World world) {
        ArrayList<BlockPos> result = new ArrayList<>();
        for (MachineStructureRecipeKeyModel model : models) {
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
        return new ResourceLocation(MM.ID, "machine_structure");
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new Serializer();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypes.MACHINE_STRUCTURE;
    }

    public static final class Serializer implements IRecipeSerializer<MachineStructureRecipe> {


        @Override
        public MachineStructureRecipe fromJson(ResourceLocation rl, JsonObject obj) {
            String controllerId = obj.get("controllerId").getAsString();
            String id = obj.get("id").getAsString();
            DataResult<Pair<List<MachineStructureRecipeKeyModel>, JsonElement>> apply = JsonOps.INSTANCE.withDecoder(Codec.list(MachineStructureRecipeKeyModel.CODEC)).apply(obj.getAsJsonArray("blocks"));
            List<MachineStructureRecipeKeyModel> first = apply.result().get().getFirst();
            return new MachineStructureRecipe(first, controllerId, id);
        }

        @Nullable
        @Override
        public MachineStructureRecipe fromNetwork(ResourceLocation rl, PacketBuffer buf) {
            String controllerId = buf.readUtf();
            String id = buf.readUtf();
            List<MachineStructureRecipeKeyModel> models = null;
            try {
                models = buf.readWithCodec(Codec.list(MachineStructureRecipeKeyModel.CODEC));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new MachineStructureRecipe(models, controllerId, id);
        }

        @Override
        public void toNetwork(PacketBuffer buf, MachineStructureRecipe recipe) {
            buf.writeUtf(recipe.controllerId);
            buf.writeUtf(recipe.id);
            try {
                buf.writeWithCodec(Codec.list(MachineStructureRecipeKeyModel.CODEC), recipe.models);
            } catch (IOException e) {
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

    }
}
