package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MachineStructureObject {
    public static final Codec<MachineStructureObject> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.list(MachineStructureRecipeKeyModel.CODEC).fieldOf("object").forGetter(z -> z.inner)
    ).apply(x, MachineStructureObject::new));

    private final List<MachineStructureRecipeKeyModel> inner;
}
