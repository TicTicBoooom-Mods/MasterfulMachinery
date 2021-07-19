package com.ticticboooom.mods.mm.ports.state;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.client.jei.MMJeiPlugin;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.PressureStack;
import com.ticticboooom.mods.mm.client.jei.ingredients.model.RotationStack;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.RotationPortStorage;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiIngredientGroup;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RotationPortState extends PortState {

    public static final Codec<RotationPortState> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.FLOAT.fieldOf("speed").forGetter(z -> z.speed)
    ).apply(x, RotationPortState::new));

    private float speed;

    public RotationPortState(float pressure) {
        this.speed = pressure;
    }

    @Override
    public void processRequirement(List<PortStorage> storage) {

    }

    @Override
    public boolean validateRequirement(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof RotationPortStorage){
                RotationPortStorage rot = (RotationPortStorage) portStorage;
                if (rot.getSpeed() >= speed) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processResult(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof RotationPortStorage){
                RotationPortStorage rot = (RotationPortStorage) portStorage;
                rot.setSpeed(speed);
            }
        }
    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        return true;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(MM.ID, "create_rotation");
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return MMJeiPlugin.PRESSURE_TYPE;
    }

    @Override
    public <T> List<T> getIngredient(boolean input) {
        return new ArrayList<>();
    }

    @Override
    public void setupRecipe(IRecipeLayout layout, Integer typeIndex, int x, int y, boolean input) {
        IGuiIngredientGroup<RotationStack> group = layout.getIngredientsGroup(MMJeiPlugin.ROT_TYPE);
        group.init(typeIndex, input, x+ 1, y +1);
        group.set(typeIndex, new RotationStack(speed));
    }

    @Override
    public void render(MatrixStack ms, int x, int y, int mouseX, int mouseY, IJeiHelpers helpers) {
        IDrawableStatic slot = helpers.getGuiHelper().getSlotDrawable();
        slot.draw(ms, x, y);
    }
}
