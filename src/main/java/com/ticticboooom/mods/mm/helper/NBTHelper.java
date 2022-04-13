package com.ticticboooom.mods.mm.helper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class NBTHelper {
    public static CompoundNBT toCompound(BlockPos pos) {
        CompoundNBT result = new CompoundNBT();
        result.putInt("x", pos.getX());
        result.putInt("y", pos.getY());
        result.putInt("z", pos.getZ());
        return result;
    }

    public static BlockPos fromCompound(CompoundNBT nbt) {
        int x = nbt.getInt("x");
        int y = nbt.getInt("y");
        int z = nbt.getInt("z");
        return new BlockPos(x, y, z);
    }
}
