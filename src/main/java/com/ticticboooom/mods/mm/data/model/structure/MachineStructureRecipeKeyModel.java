package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeKeyModel {

    public static final Codec<MachineStructureRecipeKeyModel> CODEC = RecordCodecBuilder.create(x -> x.group(
        MachineStructureBlockPos.CODEC.fieldOf("pos").forGetter(z -> z.pos),
        Codec.either(MachineStructureRecipeData.CODEC, Codec.list(MachineStructureRecipeData.CODEC)).xmap(
            either -> either.map(Collections::singletonList, Function.identity()),
            data -> data.size() == 1 ? Either.left(data.get(0)) : Either.right(data)
        ).fieldOf("data").forGetter(z -> z.data)
    ).apply(x, MachineStructureRecipeKeyModel::new));

    private MachineStructureBlockPos pos;
    private final List<MachineStructureRecipeData> data;
}
