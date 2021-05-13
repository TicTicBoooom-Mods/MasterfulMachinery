package com.ticticboooom.mods.mm.helper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.system.CallbackI;

public class NBTHelper {
    public static CompoundNBT toCompound(BlockPos pos) {
        CompoundNBT result = new CompoundNBT();
        result.putInt("x", pos.getX());
        result.putInt("y", pos.getY());
        result.putInt("z", pos.getZ());
        return result;
    }
}
