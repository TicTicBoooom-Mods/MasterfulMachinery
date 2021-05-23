package com.ticticboooom.mods.mm.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class ModelOverrideModel {
    public static final Codec<ModelOverrideModel> CODEC  = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("commonModel").forGetter(z -> z.commonModel),
            Codec.STRING.optionalFieldOf("inputModel").forGetter(z -> z.inputModel),
            Codec.STRING.optionalFieldOf("outputModel").forGetter(z -> z.outputModel)
    ).apply(x, ModelOverrideModel::new));

    private final Optional<String> commonModel;
    private final Optional<String> inputModel;
    private final Optional<String> outputModel;
}
