package com.ticticboooom.mods.mm.block.tile;

import com.simibubi.create.content.contraptions.KineticNetwork;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.network.PacketHandler;
import com.ticticboooom.mods.mm.network.packets.TileClientUpdatePacket;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.RotationPortStorage;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class RotationGenMachinePortBlockEntity extends GeneratingKineticTileEntity implements IMachinePortTile, ITickableTileEntity {
    private final ContainerType<?> container;
    @Getter
    private final PortStorage storage;
    @Getter
    private boolean input;

    public RotationGenMachinePortBlockEntity(TileEntityType<?> typeIn, ContainerType<?> container, PortStorage storage, boolean input) {
        super(typeIn);
        this.container = container;
        this.storage = storage;
        this.input = input;
        if (input) {
            this.stress = ((RotationPortStorage) storage).getStress();
        }

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.masterfulmachinery.create_rotation");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new PortBlockContainer(container, p_createMenu_1_, p_createMenu_2_, this);
    }

    @Override
    public void tick() {
        this.reActivateSource = true;
        super.tick();
        this.storage.tick(this);
//        if (storage instanceof RotationPortStorage) {
//            RotationPortStorage stor = (RotationPortStorage) this.storage;
//            float prev = this.speed;
//            float speed = stor.getSpeed();
//            if (speed != prev) {
//                if (!hasSource()) {
//                    effects.queueRotationIndicators();
//                }
//                applyNewSpeed(prev, speed);
//            }
//            if (hasNetwork() && speed != 0) {
//                KineticNetwork network = getOrCreateNetwork();
//                notifyStressCapacityChange(calculateAddedStressCapacity());
//                getOrCreateNetwork().updateCapacityFor(this, calculateStressApplied());
//                network.updateStress();
//            }
//
//            onSpeedChanged(prev);
//            sendData();
//        }

        if (!world.isRemote()) {
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new TileClientUpdatePacket.Data(pos, write(new CompoundNBT())));
        }
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        this.storage.load(compound.getCompound("inv"));
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        compound.put("inv", this.storage.save(new CompoundNBT()));
        super.write(compound, clientPacket);
    }


    @Override
    public float getGeneratedSpeed() {
        if (storage instanceof RotationPortStorage) {
            RotationPortStorage stor = (RotationPortStorage) this.storage;
            return stor.getSpeed();
        }
        return 0;
    }
}
