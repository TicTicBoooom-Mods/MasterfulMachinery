package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.ports.storage.ItemPortStorage;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

public class ItemPortState extends PortState {

    public static final Codec<ItemPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("count").forGetter(z -> z.count),
            Codec.STRING.optionalFieldOf("item").forGetter(z -> Optional.ofNullable(z.item)),
            Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.ofNullable(z.tag))
    ).apply(x, (c, i, t) -> new ItemPortState(c, i.orElse(""), t.orElse(""))));

    @Getter
    private final int count;
    private final String item;
    private final String tag;

    public ItemPortState(int count, String item, String tag) {
        this.count = count;
        this.item = item;
        this.tag = tag;
    }

    @Override
    public void processRequirement(List<IPortStorage> storage) {
        int current = count;
        for (IPortStorage inv : storage) {
            if (inv instanceof ItemPortStorage) {
                ItemPortStorage iinv = (ItemPortStorage) inv;
                for (int i = 0; i < iinv.getInv().getSlots(); i++) {
                    ItemStack stackInSlot = iinv.getInv().getStackInSlot(i);
                    if (stackInSlot.isEmpty()) {
                        continue;
                    }

                    if (!item.equals("")) {
                        if (stackInSlot.getItem().getRegistryName().toString().equals(item)) {
                            int amount = stackInSlot.getCount();
                            stackInSlot.setCount(amount - (amount - current < 0 ? amount : current));
                            current -= amount;
                        }
                    } else if (!tag.equals("")) {
                        if (ItemTags.getAllTags().getTag(RLUtils.toRL(tag)).contains(stackInSlot.getItem())) {
                            int amount = stackInSlot.getCount();
                            stackInSlot.setCount(amount - (amount - current < 0 ? amount : current));
                            current -= amount;

                        }
                    }
                    if (current <= 0) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean validateRequirement(List<IPortStorage> storage) {
        int current = count;
        for (IPortStorage inv : storage) {
            if (inv instanceof ItemPortStorage) {
                ItemPortStorage iinv = (ItemPortStorage) inv;
                 for (int i = 0; i < iinv.getInv().getSlots(); i++) {
                    ItemStack stackInSlot = iinv.getInv().getStackInSlot(i);
                    if (stackInSlot.isEmpty()) {
                        continue;
                    }

                    if (!item.equals("")) {
                        if (stackInSlot.getItem().getRegistryName().toString().equals(item)) {
                            current -= stackInSlot.getCount();
                        }
                    } else if (!tag.equals("")) {
                        ITag<Item> tag = ItemTags.getAllTags().getTag(RLUtils.toRL(this.tag));
                        if (tag != null && tag.contains(stackInSlot.getItem())) {
                            current -= stackInSlot.getCount();
                        }
                    }
                    if (current <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<IPortStorage> storage) {
        int current = 0;
        for (IPortStorage inv : storage) {
            if (inv instanceof ItemPortStorage) {
                ItemPortStorage iinv = (ItemPortStorage) inv;
                for (int i = 0; i < iinv.getInv().getSlots(); i++) {
                    ItemStack stackInSlot = iinv.getInv().getStackInSlot(i);

                    if (!item.equals("")) {
                        if (stackInSlot.getItem().getRegistryName().toString().equals(item)) {
                            int amount = stackInSlot.getCount();
                            int increment = Math.min((stackInSlot.getItem().getMaxStackSize() - amount), (count - current));
                            stackInSlot.setCount(stackInSlot.getCount() + increment);
                            current += increment;
                        } else if (stackInSlot.isEmpty()) {
                            Item forgeItem = ForgeRegistries.ITEMS.getValue(RLUtils.toRL(item));
                            iinv.getInv().setStackInSlot(i, new ItemStack(forgeItem, Math.min(forgeItem.getMaxStackSize(), count - current)));
                            current += Math.min(forgeItem.getMaxStackSize(), count - current);
                        }
                    }
                    if (current >= count) {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean validateResult(List<IPortStorage> storage) {
        int current = 0;
        for (IPortStorage inv : storage) {
            if (inv instanceof ItemPortStorage) {
                ItemPortStorage iinv = (ItemPortStorage) inv;
                for (int i = 0; i < iinv.getInv().getSlots(); i++) {
                    ItemStack stackInSlot = iinv.getInv().getStackInSlot(i);
                    if (stackInSlot.isEmpty()) {
                        if (!item.equals("")) {
                            current += ForgeRegistries.ITEMS.getValue(RLUtils.toRL(item)).getMaxStackSize();
                        }
                        if (current >= count) {
                            return true;
                        }
                        continue;
                    }

                    if (!item.equals("")) {
                        if (stackInSlot.getItem().getRegistryName().toString().equals(item)) {
                            current += stackInSlot.getMaxStackSize() - stackInSlot.getCount();
                        }
                    }
                    if (current >= count) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "items");
    }
}
