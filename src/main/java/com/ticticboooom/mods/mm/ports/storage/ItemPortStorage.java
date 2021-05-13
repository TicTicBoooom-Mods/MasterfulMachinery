package com.ticticboooom.mods.mm.ports.storage;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.container.PortBlockContainer;
import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.helper.InvHelper;
import com.ticticboooom.mods.mm.inventory.ItemStackInventory;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemPortStorage extends PortStorage {
    public static final Codec<ItemPortStorage> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("rows").forGetter(z -> z.rows),
            Codec.INT.fieldOf("columns").forGetter(z -> z.columns)
    ).apply(x, ItemPortStorage::new));

    @Getter
    private final ItemStackHandler inv;
    @Getter
    private final int rows;
    @Getter
    private final int columns;

    private final LazyOptional<ItemStackHandler> invLO;

    public ItemPortStorage(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.inv = new ItemStackHandler(rows * columns);
        this.invLO = LazyOptional.of(() -> this.inv);
    }


    @Override
    public <T> LazyOptional<T> getLO() {
        return invLO.cast();
    }

    @Override
    public <T> boolean validate(Capability<T> cap) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        CompoundNBT compoundNBT = inv.serializeNBT();
        nbt = nbt.merge(compoundNBT);
        return nbt;
    }

    @Override
    public void load(CompoundNBT nbt) {
        inv.deserializeNBT(nbt);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, int left, int top, Screen screen) {
        Minecraft.getInstance().textureManager.bind(new ResourceLocation(MM.ID, "textures/gui/port_gui.png"));
        screen.blit(stack, left, top, 0, 0, 175, 256);
        int offsetY = ((108 - (rows * 18)) / 2) + 7;
        int offsetX = ((162 - (columns * 18)) / 2) + 7;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                screen.blit(stack, left + (x * 18) + offsetX, top + (y * 18) + offsetY, 175, 256, 18, 18);
            }
        }
    }

    @Override
    public void setupContainer(PortBlockContainer container, PlayerInventory inv, MachinePortBlockEntity tile) {
        int offsetX = ((162 - (columns * 18)) / 2) + 8;
        int offsetY = ((108 - (rows * 18)) / 2) + 8;
        ItemStackInventory items = InvHelper.getItems(this.inv);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                container.addSlot(new Slot(items,  (y * columns) + x, x * 18 + offsetX, y * 18 + offsetY));
            }
        }
    }




}
