package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class MachineStructureRecipeData {

    public static final Codec<MachineStructureRecipeData> CODEC = RecordCodecBuilder.create(x -> x.group(
        Codec.STRING.optionalFieldOf("tag").forGetter(z -> Optional.of(z.tag)),
        Codec.STRING.optionalFieldOf("block").forGetter(z -> Optional.of(z.block)),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("props").forGetter(z -> Optional.ofNullable(z.properties)),
        MachineStructurePort.CODEC.optionalFieldOf("port").forGetter(z -> Optional.ofNullable(z.port))
    ).apply(x, (tag, block, props, port) -> new MachineStructureRecipeData(tag.orElse(""), block.orElse(""), props.orElse(null), port.orElse(null))));

    private final String tag;
    private final String block;
    private final Map<String, String> properties;
    private final MachineStructurePort port;
}
