package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.client.helper.AirBlockReader;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class BlockStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        if (json.isJsonPrimitive()) {
            return true;
        }
        if (json.isJsonObject()) {
            if (json.getAsJsonObject().has("include")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        Value result = new Value();
        result.blockSelector = new ArrayList<>();
        if (json.isJsonPrimitive()) {
            result.blockSelector.add(json.getAsString());
            return result;
        }
        if (json.isJsonObject()) {
            JsonObject jsonObj = json.getAsJsonObject();
            JsonElement includesJson = jsonObj.get("include");
            if (includesJson.isJsonPrimitive()) {
                result.blockSelector.add(includesJson.getAsString());
                return result;
            }

            JsonArray includes = includesJson.getAsJsonArray();
            includes.forEach(x -> result.blockSelector.add(x.getAsString()));
            result.properties = ParserUtils.parseOrDefault(jsonObj, "properties", x -> {
                HashMap<String, String> res = new HashMap<>();
                JsonObject obj = x.getAsJsonObject();
                obj.entrySet().forEach(z -> {
                    res.put(z.getKey(), z.getValue().getAsString());
                });
                return res;
            }, new HashMap<>());
        }
        return result;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        BlockStructureKeyType.Value data = (BlockStructureKeyType.Value) dataIn;
        boolean matches = false;
        for (String s : data.blockSelector) {
            if (s.startsWith("#")) {
                String tagName = s.substring(1);
                if (state.isIn(BlockTags.getCollection().getTagByID(Objects.requireNonNull(ResourceLocation.tryCreate(tagName))))) {
                    matches = true;
                }
            } else {
                if (state.getBlock().getRegistryName().toString().equals(s)) {
                    matches = true;
                }
            }
        }
        return matches;
    }

    @Override
    public void onBlueprintInitialRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        data.renderTicker = 0;
        data.renderBlockList = new ArrayList<>();
        for (String s : data.blockSelector) {
            if (s.startsWith("#")) {
                String tagName = s.substring(1);
                ITag<Block> tagByID = BlockTags.getCollection().getTagByID(Objects.requireNonNull(ResourceLocation.tryCreate(tagName)));
                List<Block> allBlocks = tagByID.getAllElements();
                for (Block allBlock : allBlocks) {
                    BlockState defaultState = allBlock.getDefaultState();
                    data.renderBlockList.add(new GuiBlockRenderBuilder(defaultState, new AirBlockReader(defaultState)).at(pos));
                }
            } else {
                Block value = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(s));
                BlockState defaultState = value.getDefaultState();
                data.renderBlockList.add(new GuiBlockRenderBuilder(defaultState, new AirBlockReader(defaultState)).at(pos));
            }
        }
    }

    @Override
    public GuiBlockRenderBuilder onBlueprintRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        GuiBlockRenderBuilder guiBlock = data.renderBlockList.get((int)data.renderTicker);
        data.renderTicker += 0.01;
        if (data.renderTicker >= data.renderBlockList.size()) {
            data.renderTicker = 0;
        }
        return guiBlock;
    }

    public static final class Value implements StructureKeyTypeValue {
        public List<String> blockSelector;
        public Map<String, String> properties;
        public float renderTicker;
        public List<GuiBlockRenderBuilder> renderBlockList;
    }
}
