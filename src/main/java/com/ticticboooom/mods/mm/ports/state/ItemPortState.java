package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.exception.InvalidProcessDefinitionException;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.storage.IPortStorage;
import com.ticticboooom.mods.mm.ports.storage.ItemPortStorage;
import lombok.Getter;
import lombok.SneakyThrows;
import mekanism.common.integration.projecte.IngredientHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @SneakyThrows
    @Override
    public void validateDefinition() {
        if (!item.equals("")){
            if (!RLUtils.isRL(item)){
                throw new InvalidProcessDefinitionException("Item: " + item + " is not a valid item id (ResourceLocation)");
            }

            if (!ForgeRegistries.ITEMS.containsKey(RLUtils.toRL(item))){
                throw new InvalidProcessDefinitionException("Item: " + item + " does not exist in the game's item registry");
            }
        } else if (!tag.equals("")){
            if (!RLUtils.isRL(tag)){
                throw new InvalidProcessDefinitionException("Item Tag: " + tag + " is not a valid item tag id (ResourceLocation)");
            }
        } else{
            throw new InvalidProcessDefinitionException("You must define a item id or item tag id in the items 'data' object");
        }
    }

    private int tagIndex = 0;
    private int tagIndexIncremement = 0;

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic slot = helpers.getGuiHelper().getSlotDrawable();
        slot.draw(ms, x, y);
    }

    @Override
    public void setIngredient(IIngredients in, boolean input) {
        if (!item.equals("") && RLUtils.isRL(item)) {
            if (input){
                in.setInput(VanillaTypes.ITEM, new ItemStack(ForgeRegistries.ITEMS.getValue(RLUtils.toRL(item)), this.count));
            } else {
                in.setOutput(VanillaTypes.ITEM, new ItemStack(ForgeRegistries.ITEMS.getValue(RLUtils.toRL(item)), this.count));
            }
        } else if (!tag.equals("") && RLUtils.isRL(tag)) {
            ITag<Item> tag = ItemTags.getAllTags().getTag(RLUtils.toRL(this.tag));
            assert tag != null;
            Stream<ItemStack> itemStackStream = tag.getValues().stream().map(x -> new ItemStack(x.getItem(), this.count));
            if (input){
                in.setInputs(VanillaTypes.ITEM, itemStackStream.collect(Collectors.toList()));
            } else {
                in.setOutputs(VanillaTypes.ITEM, itemStackStream.collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        layout.getItemStacks().init(typeIndex, input, x, y);
        if (!item.equals("") && RLUtils.isRL(item)) {
            layout.getItemStacks().set(typeIndex, new ItemStack(ForgeRegistries.ITEMS.getValue(RLUtils.toRL(item)), this.count));
        } else if (!tag.equals("") && RLUtils.isRL(tag)) {
            ITag<Item> tag = ItemTags.getAllTags().getTag(RLUtils.toRL(this.tag));
            assert tag != null;
            Stream<ItemStack> itemStackStream = tag.getValues().stream().map(z -> new ItemStack(z.getItem(), this.count));
            layout.getItemStacks().set(typeIndex, itemStackStream.collect(Collectors.toList()));
    }
    }
}
