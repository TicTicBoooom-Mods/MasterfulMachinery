package com.ticticboooom.mods.mm.nbt;

import net.minecraft.nbt.*;

public class NBTUtils {
    public static boolean containsKey(CompoundNBT tag, String key) {
        if (key.contains(":")) {
            int i = key.indexOf(":");
            if (i >= key.length() - 1) {
                return false;
            }
            String remains = key.substring(i + 1, key.length() - 1);
            String current = key.substring(0, i);
            if (tag.contains(current)) {
                return containsKey(tag, remains);
            } else {
                return false;
            }
        }
        return tag.contains(key);
    }

    public static INBT getValue(CompoundNBT tag, String key) {
        if (key.contains(":")) {
            int i = key.indexOf(":");
            if (i >= key.length() - 1) {
                return null;
            }
            String remains = key.substring(i + 1, key.length() - 1);
            String current = key.substring(0, i);
            if (tag.contains(current)) {
                return getValue(tag.getCompound(current), remains);
            } else {
                return null;
            }
        }
        return tag.get(key);
    }

    public static CompoundNBT setValue(CompoundNBT tag, String key, CompoundNBT value) {
        if (key.contains(":")) {
            int i = key.indexOf(":");
            if (i >= key.length() - 1) {
                return null;
            }
            String remains = key.substring(i + 1, key.length() - 1);
            String current = key.substring(0, i);
            if (!tag.contains(current)) {
                tag.put(current, new CompoundNBT());
            }
            return setValue(tag.getCompound(current), remains, value);
        }
        tag.put(key, value);
        return tag;
    }
}
