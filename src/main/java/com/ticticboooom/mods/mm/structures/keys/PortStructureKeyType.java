package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.setup.MMItems;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import com.ticticboooom.mods.mm.util.GuiBlockUtils;
import com.ticticboooom.mods.mm.util.TagHelper;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PortStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.PORT.toString());
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        Value result = new Value();
        JsonObject obj = json.getAsJsonObject();
        result.port = ResourceLocation.tryCreate(obj.get("port").getAsString());
        result.input = ParserUtils.parseOrDefault(obj, "input", x -> Optional.of(x.getAsBoolean()), Optional.empty());
        return result;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        PortStructureKeyType.Value data = (PortStructureKeyType.Value) dataIn;
        if (state.getBlock().getRegistryName().equals(MMBlocks.PORT.getId())) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof PortTile) {
                PortTile pte = (PortTile) te;
                if (pte.portModel == null || pte.portModel.id == null) {
                    return false;
                }
                boolean io = true;
                if (data.input.isPresent()) {
                    io = data.input.get() == pte.portModel.input;
                }
                return io && pte.portModel.type.equals(data.port);
            }
        }
        return false;
    }

    @Override
    public void onBlueprintInitialRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        data.renderTicker = 0;
        data.renderBlocks = new ArrayList<>();
        data.renderItemList = new ArrayList<>();
        for (Map.Entry<ResourceLocation, PortModel> entry : DataRegistry.PORTS.entrySet()) {
            if (entry.getValue().type.equals(data.port)) {
                GuiBlockRenderBuilder guiBlockPort = GuiBlockUtils.getGuiBlockPort(pos, entry.getValue().id);
                data.renderBlocks.add(guiBlockPort);
                ItemStack itemStack = new ItemStack(MMItems.PORT.get());
                TagHelper.setPortId(itemStack, entry.getValue().id);
                data.renderItemList.add(itemStack);
            }
        }
    }

    @Override
    public GuiBlockRenderBuilder onBlueprintRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        GuiBlockRenderBuilder guiBlock = data.renderBlocks.get((int)data.renderTicker);
        data.renderTicker += 0.01;
        if (data.renderTicker >= data.renderBlocks.size()) {
            data.renderTicker = 0;
        }
        return guiBlock;
    }

    @Override
    public ItemStack onBlueprintListRender(StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        return data.renderItemList.get((int)data.renderTicker);
    }

    public static final class Value implements StructureKeyTypeValue {
        public ResourceLocation  port;
        public Optional<Boolean> input;

        public List<GuiBlockRenderBuilder> renderBlocks;
        public List<ItemStack> renderItemList;

        public float renderTicker;
    }
}
