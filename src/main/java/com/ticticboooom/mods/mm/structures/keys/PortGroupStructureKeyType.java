package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

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
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world) {
        PortGroupStructureKeyType.Value data = (PortGroupStructureKeyType.Value) dataIn;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof PortTile) {
            PortTile pte = (PortTile) te;
            List<String> reqKeys = model.portGroupings.get(data.group);
            for (String reqKey : reqKeys) {
                StructureModel.RequiredPort requiredPort = model.requiredPorts.get(reqKey);
                if (requiredPort != null && !requiredPort.port.equals(pte.portModel.type)) {
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

    public static final class Value implements StructureKeyTypeValue {
        public String group;
    }
}
