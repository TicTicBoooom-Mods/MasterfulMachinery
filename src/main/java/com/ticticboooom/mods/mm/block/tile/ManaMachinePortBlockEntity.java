package com.ticticboooom.mods.mm.block.tile;

import com.ticticboooom.mods.mm.ports.storage.ManaPortStorage;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

public class ManaMachinePortBlockEntity extends MachinePortBlockEntity implements IManaPool, ISparkAttachable {

    private ISparkEntity spark;

    public ManaMachinePortBlockEntity(TileEntityType<?> p_i48289_1_, ContainerType<?> container, PortStorage storage, boolean input) {
        super(p_i48289_1_, container, storage, input);
    }

    @Override
    public boolean isFull() {
        if (storage instanceof ManaPortStorage) {
            ManaPortStorage s = (ManaPortStorage) storage;
            return s.getInv().getManaStored() >= s.getInv().getMaxManaStored();
        }
        return true;
    }

    @Override
    public void receiveMana(int mana) {
        if (storage instanceof ManaPortStorage) {
            ManaPortStorage s = (ManaPortStorage) storage;
            if (mana > 0) {
                s.getInv().receiveMana(mana, false);
            } else {
                s.getInv().extractMana(-mana, false);
            }
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return this.isInput();
    }

    @Override
    public int getCurrentMana() {
        if (storage instanceof ManaPortStorage) {
            ManaPortStorage s = (ManaPortStorage) storage;
            return s.getInv().getManaStored();
        }
        return 0;
    }

    @Override
    public boolean isOutputtingPower() {
        return !this.input;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.GRAY;
    }

    @Override
    public void setColor(DyeColor color) {

    }

    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    public void attachSpark(ISparkEntity entity) {
        spark = entity;
    }

    @Override
    public int getAvailableSpaceForMana() {
        if (storage instanceof ManaPortStorage) {
            ManaPortStorage s = (ManaPortStorage) storage;
            return s.getInv().getMaxManaStored() - s.getInv().getManaStored();
        }
        return 0;
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        return spark;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }
}
