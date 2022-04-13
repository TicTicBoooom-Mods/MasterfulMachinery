package com.ticticboooom.mods.mm.data.model.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.math.vector.Vector3i;

@Getter
@AllArgsConstructor
public class MachineStructureBlockPos implements Comparable<MachineStructureBlockPos> {

    public static final Codec<MachineStructureBlockPos> CODEC = RecordCodecBuilder.create(x -> x.group(
            Codec.INT.fieldOf("x").forGetter(z -> z.x),
            Codec.INT.fieldOf("y").forGetter(z -> z.y),
            Codec.INT.fieldOf("z").forGetter(z -> z.z)
    ).apply(x, MachineStructureBlockPos::new));

    private final int x;
    private final int y;
    private final int z;

    @Override
    public int compareTo(MachineStructureBlockPos other) {
        return toVector().compareTo(other.toVector());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MachineStructureBlockPos that = (MachineStructureBlockPos) o;
        return toVector().equals(that.toVector());
    }

    @Override
    public int hashCode() {
        return toVector().hashCode();
    }

    public Vector3i toVector(){
        return new Vector3i(this.x, this.y, this.z);
    }
}