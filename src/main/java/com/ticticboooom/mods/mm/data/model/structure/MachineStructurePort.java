package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ToString
@Getter
@AllArgsConstructor
public class MachineStructurePort {
    public static final Codec<MachineStructurePort> CODEC = RecordCodecBuilder.create(x -> x.group(
        Codec.STRING.fieldOf("type").forGetter(z -> z.type),
        Codec.BOOL.fieldOf("input").forGetter(z -> z.input),
        Codec.either(Codec.STRING, Codec.list(Codec.STRING)).xmap(
            either -> either.map(Collections::singletonList, Function.identity()),
            ids -> ids.size() == 1 ? Either.left(ids.get(0)) : Either.right(ids)
        ).optionalFieldOf("controllerId").forGetter(z -> Optional.ofNullable(z.controllerId))
    ).apply(x, (w, y, z) -> new MachineStructurePort(w, y, z.orElse(null))));

    private final String type;
    private final boolean input;
    private final List<String> controllerId;
}
