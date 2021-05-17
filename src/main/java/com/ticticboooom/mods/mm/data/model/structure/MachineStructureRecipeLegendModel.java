package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeLegendModel {
    public static final Codec<MachineStructureRecipeLegendModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.tag)),
            Codec.STRING.optionalFieldOf("block").forGetter(z -> Optional.of(z.block)),
            Codec.STRING.optionalFieldOf("nbt").forGetter(z -> Optional.of(z.nbt))
    ).apply(x, (w, y, z) -> new MachineStructureRecipeLegendModel(w.orElse(""), y.orElse(""), z.orElse(""))));
    private final String tag;
    private final String block;
    private final String nbt;
}
