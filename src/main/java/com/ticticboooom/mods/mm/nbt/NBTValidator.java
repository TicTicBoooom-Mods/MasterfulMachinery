package com.ticticboooom.mods.mm.nbt;

import com.ticticboooom.mods.mm.nbt.model.NBTActionModel;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;
import lombok.SneakyThrows;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;

public class NBTValidator {
    public static boolean isValid(CompoundNBT tag, NBTModel requirements) {
        for (NBTActionModel action : requirements.getActions()) {
            if (!innerIsValid(tag, action)){
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    private static boolean innerIsValid(CompoundNBT tag, NBTActionModel model) {
        if (model.getAction().equals("contains_key")) {
            return NBTUtils.containsKey(tag, model.getKey());
        }
        if (model.getAction().equals("contains_val")) {
            INBT value = NBTUtils.getValue(tag, model.getKey());
            CompoundNBT valueTag = JsonToNBT.getTagFromJson(model.getValue().toString());
            return NBTUtil.areNBTEquals(value, valueTag, false);
        }
        if (model.getAction().equals("exact_eq")){
            INBT value = NBTUtils.getValue(tag, model.getKey());
            CompoundNBT valueTag = JsonToNBT.getTagFromJson(model.getValue().toString());
            return NBTUtil.areNBTEquals(value, valueTag, true);
        }
        return false;
    }
}
