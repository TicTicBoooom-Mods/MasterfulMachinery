package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.client.helper.AirBlockReader;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.PortModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
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

public class PortGroupStructureKeyType extends StructureKeyType {

    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.PORT_GROUP.toString());
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        JsonObject obj = json.getAsJsonObject();
        Value result = new Value();
        result.group = obj.get("group").getAsString();
        return result;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        PortGroupStructureKeyType.Value data = (PortGroupStructureKeyType.Value) dataIn;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof PortTile) {
            PortTile pte = (PortTile) te;
            if (pte.portModel == null || pte.portModel.id == null) {
                return false;
            }
            List<String> reqKeys = model.portGroupings.get(data.group);
            for (String reqKey : reqKeys) {
                StructureModel.RequiredPort requiredPort = model.requiredPorts.get(reqKey);
                if (requiredPort != null && requiredPort.port != null && !requiredPort.port.equals(pte.portModel.type)) {
                    return false;
                }
                boolean matches = false;
                for (ResourceLocation tier : requiredPort.tiers) {
                    if (tier.toString().equals(pte.portModel.id.toString())) {
                        matches = true;
                    }
                }
                return matches;
            }
        }
        return false;
    }

    @Override
    public void onBlueprintInitialRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        data.renderTicker = 0;
        data.renderBlockList = new ArrayList<>();
        data.renderItemList = new ArrayList<>();
        List<String> portKeys = model.portGroupings.get(data.group);
        for (String key : portKeys) {
            StructureModel.RequiredPort requiredPort = model.requiredPorts.get(key);
            if (requiredPort.tiers.isEmpty()) {
                for (Map.Entry<ResourceLocation, PortModel> entry : DataRegistry.PORTS.entrySet()) {
                    if (entry.getValue().type.equals(requiredPort.port)) {
                        GuiBlockRenderBuilder guiBlockPort = GuiBlockUtils.getGuiBlockPort(pos, entry.getValue().id);
                        data.renderBlockList.add(guiBlockPort);
                        ItemStack itemStack = new ItemStack(MMItems.PORT.get());
                        TagHelper.setPortId(itemStack, entry.getValue().id);
                        data.renderItemList.add(itemStack);
                    }
                }
            } else {

                for (ResourceLocation tier : requiredPort.tiers) {
                    GuiBlockRenderBuilder guiBlockPort = GuiBlockUtils.getGuiBlockPort(pos, tier);
                    data.renderBlockList.add(guiBlockPort);
                    ItemStack itemStack = new ItemStack(MMItems.PORT.get());
                    TagHelper.setPortId(itemStack, tier);
                    data.renderItemList.add(itemStack);
                }
            }
        }
    }

    @Override
    public GuiBlockRenderBuilder onBlueprintRender(BlockPos pos, StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        GuiBlockRenderBuilder guiBlock = data.renderBlockList.get((int) data.renderTicker);
        data.renderTicker += 0.01;
        if (data.renderTicker >= data.renderBlockList.size()) {
            data.renderTicker = 0;
        }
        return guiBlock;
    }

    @Override
    public ItemStack onBlueprintListRender(StructureModel model, StructureKeyTypeValue dataIn) {
        Value data = (Value) dataIn;
        return data.renderItemList.get((int) data.renderTicker);
    }

    public static final class Value implements StructureKeyTypeValue {
        public String group;
        // render
        public float renderTicker;
        public List<GuiBlockRenderBuilder> renderBlockList;
        public List<ItemStack> renderItemList;

    }
}
