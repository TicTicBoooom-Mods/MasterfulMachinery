package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeKeyModel {
    public static final Codec<MachineStructureRecipeKeyModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            MachineStructureBlockPos.CODEC.fieldOf("pos").forGetter(z -> z.pos),
            Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.tag)),
            Codec.STRING.optionalFieldOf("block").forGetter(z -> Optional.of(z.block)),
            Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.nbt))
    ).apply(x, (w0, w, y, z) -> new MachineStructureRecipeKeyModel(w0, w.orElse(""), y.orElse(""), z.orElse(""))));
    private  MachineStructureBlockPos pos;
    private final String tag;
    private final String block;
    private final String nbt;
}
