package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeLegendModel {
    public static final Codec<MachineStructureRecipeLegendModel> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.tag)),
            Codec.STRING.optionalFieldOf("block").forGetter(z -> Optional.of(z.block)),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("props").forGetter(z -> Optional.ofNullable(z.properties))
    ).apply(x, (w, y, z) -> new MachineStructureRecipeLegendModel(w.orElse(""), y.orElse(""), z.orElse(null))));
    private final String tag;
    private final String block;

    @Setter
    private Map<String, String> properties;
}
