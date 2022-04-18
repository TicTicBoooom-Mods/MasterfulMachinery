package com.ticticboooom.mods.mm.structures.keys;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.tile.PortTile;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.data.util.ParserUtils;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class PortTierStructureKeyType extends StructureKeyType {
    @Override
    public boolean matches(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("type").getAsString();
        return type.equals(Ref.Reg.SKT.PORT_TIER.toString());
    }

    @Override
    public StructureKeyTypeValue parse(JsonElement json, List<ResourceLocation> controllerIds, ResourceLocation structureId) {
        Value result = new Value();
        JsonObject obj = json.getAsJsonObject();
        result.portTier = ResourceLocation.tryCreate(obj.get("portTier").getAsString());
        result.input = ParserUtils.parseOrDefault(obj, "input", x -> Optional.of(x.getAsBoolean()), Optional.empty());
        return result;
    }

    @Override
    public boolean isValidPlacement(BlockPos pos, StructureModel model, BlockState state, StructureKeyTypeValue dataIn, World world, MachineStructureContext ctx) {
        PortTierStructureKeyType.Value data = (PortTierStructureKeyType.Value) dataIn;
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
                return io && pte.portModel.id.equals(data.portTier);
            }
        }
        return false;
    }

    public static final class Value implements StructureKeyTypeValue {
        public ResourceLocation portTier;
        public Optional<Boolean> input;
    }
}
