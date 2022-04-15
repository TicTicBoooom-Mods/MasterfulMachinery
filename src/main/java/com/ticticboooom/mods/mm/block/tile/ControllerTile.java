package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.ter.model.controller.ControllerBlockModel;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.setup.MMTiles;
import com.ticticboooom.mods.mm.structures.StructureKeyTypeValue;
import com.ticticboooom.mods.mm.structures.keys.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ControllerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public ControllerTile() {
        super(MMTiles.CONTROLLER.get());
    }

    public ControllerModel controllerModel;
    public String status = "";

    @Override
    public void tick() {
        if (controllerModel == null) {
            return;
        }
        StructureModel valid = getValid();
        if (valid == null) {
            return;
        }
    }

    private StructureModel getValid() {
        for (Map.Entry<ResourceLocation, StructureModel> entry : DataRegistry.STRUCTURES.entrySet()) {
            List<ResourceLocation> controllerId = entry.getValue().controllerId;
            if (!controllerIdMatches(controllerId)) {
                continue;
            }

            if (isValidBlockPlacement(entry.getValue())) {
                status = "Structure Found!";
                return entry.getValue();
            }
        }
        status = "Structure Not Found!";
        return null;
    }

    private boolean controllerIdMatches(List<ResourceLocation> ids) {
        for (ResourceLocation id : ids) {
            if (id.equals(controllerModel.id)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidBlock(StructureKeyTypeValue dataIn, StructureModel model, BlockState blockState, BlockPos pos) {
        BlockStructureKeyType.Value data = (BlockStructureKeyType.Value) dataIn;
        boolean matches = false;
        for (String s : data.blockSelector) {
            if (s.startsWith("#")) {
                String tagName = s.substring(1);
                if (blockState.isIn(BlockTags.getCollection().getTagByID(Objects.requireNonNull(ResourceLocation.tryCreate(tagName))))) {
                    matches = true;
                }
            } else {
                if (blockState.getBlock().getRegistryName().toString().equals(s)) {
                    matches = true;
                }
            }
        }
        return matches;
    }

    private boolean isValidPort(StructureKeyTypeValue dataIn, StructureModel model, BlockState blockState, BlockPos pos) {
        PortStructureKeyType.Value data = (PortStructureKeyType.Value) dataIn;
        if (blockState.getBlock().getRegistryName().equals(MMBlocks.PORT.getId())) {
            TileEntity te = world.getTileEntity(this.pos.add(pos));
            if (te instanceof PortTile) {
                PortTile pte = (PortTile) te;
                boolean io = true;
                if (data.input.isPresent()) {
                    io = data.input.get() == pte.portModel.input;
                }
                return io && pte.portModel.type.equals(data.port);
            }
        }
        return false;
    }

    private boolean isValidPortTier(StructureKeyTypeValue dataIn, StructureModel model, BlockState blockState, BlockPos pos) {
        PortTierStructureKeyType.Value data = (PortTierStructureKeyType.Value) dataIn;
        if (blockState.getBlock().getRegistryName().equals(MMBlocks.PORT.getId())) {
            TileEntity te = world.getTileEntity(this.pos.add(pos));
            if (te instanceof PortTile) {
                PortTile pte = (PortTile) te;
                boolean io = true;
                if (data.input.isPresent()) {
                    io = data.input.get() == pte.portModel.input;
                }
                return io && pte.portModel.id.equals(data.portTier);
            }
        }
        return false;
    }

    private boolean isValidPortGroup(StructureKeyTypeValue dataIn, StructureModel model, BlockState blockState, BlockPos pos) {
        PortGroupStructureKeyType.Value data = (PortGroupStructureKeyType.Value) dataIn;
        TileEntity te = world.getTileEntity(this.pos.add(pos));
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

    private boolean isValidModifiable(StructureKeyTypeValue dataIn, StructureModel model, BlockState blockState, BlockPos pos) {
        ModifiableStructureKeyType.Value data = (ModifiableStructureKeyType.Value) dataIn;
        for (Map.Entry<String, StructureKeyTypeValue> entry : data.modifiers.entrySet()) {
            if (isValidBlockSingleWithData(entry.getValue(), model, pos)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidBlockSingleWithKey(StructureModel.PositionedKey positionedKey, StructureModel model) {
        BlockState blockState = world.getBlockState(this.pos.add(positionedKey.pos));
        if (positionedKey.type.equals(Ref.Reg.SKT.BLOCK)) {
            return isValidBlock(positionedKey.data, model, blockState, positionedKey.pos);
        }
        if (positionedKey.type.equals(Ref.Reg.SKT.PORT)) {
            return isValidPort(positionedKey.data, model, blockState, positionedKey.pos);
        }
        if (positionedKey.type.equals(Ref.Reg.SKT.PORT_TIER)) {
            return isValidPortTier(positionedKey.data, model, blockState, positionedKey.pos);
        }
        if (positionedKey.type.equals(Ref.Reg.SKT.PORT_GROUP)) {
            return isValidPortGroup(positionedKey.data, model, blockState, positionedKey.pos);
        }
        if (positionedKey.type.equals(Ref.Reg.SKT.MODIFIABLE)) {
            return isValidModifiable(positionedKey.data, model, blockState, positionedKey.pos);
        }
        return true;
    }

    private boolean isValidBlockSingleWithData(StructureKeyTypeValue dataIn, StructureModel model, BlockPos pos) {
        BlockState blockState = world.getBlockState(this.pos.add(pos));
        if (dataIn instanceof BlockStructureKeyType.Value) {
            return isValidBlock(dataIn, model, blockState, pos);
        }
        if (dataIn instanceof PortStructureKeyType.Value) {
            return isValidPort(dataIn, model, blockState, pos);
        }
        if (dataIn instanceof PortTierStructureKeyType.Value) {
            return isValidPortTier(dataIn, model, blockState, pos);
        }
        if (dataIn instanceof PortGroupStructureKeyType.Value) {
            return isValidPortGroup(dataIn, model, blockState, pos);
        }
        if (dataIn instanceof ModifiableStructureKeyType.Value) {
            return isValidModifiable(dataIn, model, blockState, pos);
        }
        return true;
    }


    private List<StructureModel.PositionedKey> rotateKeys(StructureModel model, Rotation rotation) {
        List<StructureModel.PositionedKey> result = new ArrayList<>();
        for (StructureModel.PositionedKey positionedKey : model.positionedKeys) {
            result.add(new StructureModel.PositionedKey(positionedKey.pos.rotate(rotation), new StructureModel.Key(positionedKey.type, positionedKey.data)));
        }
        return result;
    }


    private boolean isValidBlockPlacement(StructureModel model) {
        Rotation rotation = Rotation.NONE;
        for (int i = 0; i < 4; i++) {
             boolean found = true;
            for (StructureModel.PositionedKey positionedKey : rotateKeys(model, rotation)) {
                if (!isValidBlockSingleWithKey(positionedKey, model)) {
                    found = false;
                }
            }
            if (found) {
                return true;
            }
            rotation = rotation.add(Rotation.CLOCKWISE_90);
        }
        return false;
    }


    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withInitial(ControllerBlockModel.CONTROLLER, controllerModel).build();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.mm.controller");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ControllerContainer(this, p_createMenu_2_, p_createMenu_1_);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (nbt.contains("ControllerId")) {
            String controllerId = nbt.getString("ControllerId");
            controllerModel = DataRegistry.CONTROLLERS.get(ResourceLocation.tryCreate(controllerId));
        }
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (controllerModel.id != null) {
            compound.putString("ControllerId", controllerModel.id.toString());
        }
        return super.write(compound);
    }
}
