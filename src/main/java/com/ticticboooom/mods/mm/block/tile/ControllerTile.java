package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.block.ter.model.controller.ControllerBlockModel;
import com.ticticboooom.mods.mm.client.container.ControllerContainer;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.ProcessModel;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.net.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.ports.ctx.MachineProcessContext;
import com.ticticboooom.mods.mm.ports.base.PortStorage;
import com.ticticboooom.mods.mm.ports.ctx.MachineStructureContext;
import com.ticticboooom.mods.mm.process.PreProcessorType;
import com.ticticboooom.mods.mm.process.ProcessIngredientType;
import com.ticticboooom.mods.mm.process.ProcessOutputType;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.setup.MMTiles;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    public ControllerTile() {
        super(MMTiles.CONTROLLER.get());
    }

    public ControllerModel controllerModel;
    public String status = "";
    public List<PortTile> activePorts = new ArrayList<>();
    public Map<String, List<String>> activeModifiers = new HashMap<>();
    private MachineProcessContext ctx;
    private MachineStructureContext sctx;

    @Override
    public void tick() {
        if (EffectiveSide.get().isServer()) {
            CompoundNBT compound = write(new CompoundNBT());
            MMNetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(this.pos, compound));
        }
        if (controllerModel == null || controllerModel.id == null) {
            activePorts.clear();
            return;
        }

        if (sctx == null) {
            sctx = new MachineStructureContext();
        }
        activeModifiers.clear();
        StructureModel valid = getValid();
        if (valid == null) {
            activePorts.clear();
            return;
        }

        if (ctx != null && ctx.processModel != null) {
            if (!validProcess(ctx.processModel, ctx)) {
                ctx = null;
            }
        }
        if (ctx == null || ctx.processModel == null) {
            ctx = new MachineProcessContext();
            List<PortStorage> inputs = new ArrayList<>();
            List<PortStorage> outputs = new ArrayList<>();
            for (PortTile activePort : activePorts) {
                if (activePort.portModel.input) {
                    inputs.add(activePort.storage);
                } else {
                    outputs.add(activePort.storage);
                }
            }

            ctx.inputs = inputs;
            ctx.outputs = outputs;
            ctx.structureId = valid.id;
            ctx.structureCtx = sctx;

            boolean found = false;
            for (Map.Entry<ResourceLocation, ProcessModel> entry : DataRegistry.PROCESSES.entrySet()) {
                ProcessModel value = entry.getValue();
                if (!validProcess(value, ctx)) {
                    continue;
                }

                ctx.processModel = value;
                ctx.ticks = value.ticks;
                ctx.processId = entry.getKey();
                for (Map.Entry<String, PreProcessorType.Value> pp : value.preprocessors.entrySet()) {
                    ResourceLocation type = pp.getValue().type;
                    PreProcessorType ppType = MMRegistries.PREPROCESSOR_TYPES.getValue(type);
                    ppType.process(pp.getValue(), ctx);
                }
                ctx.maxTicks = ctx.ticks;
                found = true;
            }
            if (!found) {
                return;
            }
        }
        if (ctx != null && ctx.processModel != null) {
            process(ctx);
        }
    }

    private boolean validProcess(ProcessModel value, MachineProcessContext ctx) {
        for (Map.Entry<String, ProcessIngredientType.Value> input : value.inputs.entrySet()) {
            ProcessIngredientType type = MMRegistries.PROCESS_INGREDIENT_TYPES.getValue(input.getValue().type);
            if (!type.canProcess(input.getValue(), ctx)) {
                return false;
            }
        }
        for (Map.Entry<String, ProcessOutputType.Value> input : value.outputs.entrySet()) {
            ProcessOutputType type = MMRegistries.PROCESS_OUTPUT_TYPES.getValue(input.getValue().type);
            if (!type.canProcess(input.getValue(), ctx)) {
                return false;
            }
        }
        return true;
    }

    private boolean process(MachineProcessContext ctx) {
        ctx.ticks--;
        if (ctx.ticks > 0) {
            status = "Processing: " + (100 - MathHelper.floor((float) ctx.ticks / ctx.maxTicks * 100)) + "%";
            return true;
        }
        for (Map.Entry<String, ProcessIngredientType.Value> input : ctx.processModel.inputs.entrySet()) {
            ProcessIngredientType type = MMRegistries.PROCESS_INGREDIENT_TYPES.getValue(input.getValue().type);
            type.process(input.getValue(), ctx);
        }
        for (Map.Entry<String, ProcessOutputType.Value> input : ctx.processModel.outputs.entrySet()) {
            ProcessOutputType type = MMRegistries.PROCESS_OUTPUT_TYPES.getValue(input.getValue().type);
            type.process(input.getValue(), ctx);
        }
        this.ctx = null;
        this.sctx = null;
        return true;
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

    private boolean isValidBlockSingleWithKey(StructureModel.PositionedKey positionedKey, StructureModel model, boolean recordPorts) {
        BlockPos abspos = this.pos.add(positionedKey.pos);
        BlockState blockState = world.getBlockState(abspos);
        if (recordPorts) {
            TileEntity tileEntity = world.getTileEntity(abspos);
            if (tileEntity instanceof PortTile) {
                activePorts.add(((PortTile) tileEntity));
            }
        }
        StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(positionedKey.type);
        return value.isValidPlacement(this.pos.add(positionedKey.pos), model, blockState, positionedKey.data, world, sctx);
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
        boolean shouldRecordPorts = activePorts.isEmpty();
        for (int i = 0; i < 4; i++) {
            boolean valid = true;
            for (StructureModel.PositionedKey positionedKey : rotateKeys(model, rotation)) {
                if (!isValidBlockSingleWithKey(positionedKey, model, shouldRecordPorts)) {
                    activePorts.clear();
                    valid = false;
                }
            }
            if (valid) {
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
        status = nbt.getString("status");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (controllerModel != null) {
            compound.putString("ControllerId", controllerModel.id.toString());
        }
        compound.putString("status", status);
        return super.write(compound);
    }
}
