package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeLegendModel {
    public static final Codec<MachineStructureRecipeLegendModel> CODEC = RecordCodecBuilder.create(x -> x.group(
        Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.tag)),
        Codec.STRING.optionalFieldOf("block").forGetter(z -> Optional.of(z.block)),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("props").forGetter(z -> Optional.ofNullable(z.properties)),
        MachineStructurePort.CODEC.optionalFieldOf("port").forGetter(z -> Optional.ofNullable(z.port))
    ).apply(x, (w, y, z, z0) -> new MachineStructureRecipeLegendModel(w.orElse(""), y.orElse(""), z.orElse(null), z0.orElse(null))));
    private final String tag;
    private final String block;
    @Setter
    private Map<String, String> properties;
    private final MachineStructurePort port;
}
