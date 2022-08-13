package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeLegendModel {

    public static final Codec<MachineStructureRecipeLegendModel> CODEC =
        Codec.either(MachineStructureRecipeData.CODEC, Codec.list(MachineStructureRecipeData.CODEC)).xmap(
            either -> either.map(Collections::singletonList, Function.identity()),
            data -> data.size() == 1 ? Either.left(data.get(0)) : Either.right(data)
        ).comapFlatMap(data -> DataResult.success(new MachineStructureRecipeLegendModel(data)), model -> model.data);

    private final List<MachineStructureRecipeData> data;
}
