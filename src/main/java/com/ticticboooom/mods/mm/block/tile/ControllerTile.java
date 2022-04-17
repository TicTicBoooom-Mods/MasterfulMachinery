package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.block.ter.model.controller.ControllerBlockModel;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.net.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.setup.MMBlocks;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.setup.MMTiles;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
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
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

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
        if (EffectiveSide.get().isServer()) {
            CompoundNBT compound = write(new CompoundNBT());
            MMNetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(this.pos, compound));
        }
        if (controllerModel == null || controllerModel.id == null) {
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

    private boolean isValidBlockSingleWithKey(StructureModel.PositionedKey positionedKey, StructureModel model) {
        BlockState blockState = world.getBlockState(this.pos.add(positionedKey.pos));
        StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(positionedKey.type);
        return value.isValidPlacement(this.pos.add(positionedKey.pos), model, blockState, positionedKey.data, world);
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
        super.read(state, nbt);
        if (nbt.contains("ControllerId")) {
            String controllerId = nbt.getString("ControllerId");
            controllerModel = DataRegistry.CONTROLLERS.get(ResourceLocation.tryCreate(controllerId));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (controllerModel != null) {
            compound.putString("ControllerId", controllerModel.id.toString());
        }
        return super.write(compound);
    }
}
