package com.ticticboooom.mods.mm.ports.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.WeatherPortStorage;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class WeatherPortState extends PortState {

    public static final Codec<WeatherPortState> CODEC= RecordCodecBuilder.create(x -> x.group(
            Codec.BOOL.optionalFieldOf("raining").forGetter(z -> z.raining),
            Codec.BOOL.optionalFieldOf("thundering").forGetter(z -> z.thundering)
    ).apply(x, WeatherPortState::new));

    private final Optional<Boolean> raining;
    private final Optional<Boolean> thundering;

    public WeatherPortState(Optional<Boolean> raining, Optional<Boolean> thundering) {
        this.raining = raining;
        this.thundering = thundering;
    }


    @Override
    public void processRequirement(List<PortStorage> storage) {

    }

    @Override
    public boolean validateRequirement(List<PortStorage> storage) {
        for (PortStorage portStorage : storage) {
            if (portStorage instanceof WeatherPortStorage){
                WeatherPortStorage weatherPortStorage = (WeatherPortStorage) portStorage;
                if (raining.isPresent() && weatherPortStorage.isRaining() != raining.get()) {
                    return false;
                }
                if (thundering.isPresent() && weatherPortStorage.isThundering() != thundering.get()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void processResult(List<PortStorage> storage) {

    }

    @Override
    public boolean validateResult(List<PortStorage> storage) {
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return null;
    }

    @Override
    public IIngredientType<?> getJeiIngredientType() {
        return null;
    }
}
