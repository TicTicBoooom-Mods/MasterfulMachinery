package com.ticticboooom.mods.mm.nbt;

import com.ticticboooom.mods.mm.nbt.model.NBTActionModel;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;
import lombok.SneakyThrows;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

public class NBTPopulate {
    public static CompoundNBT populate(CompoundNBT tag, NBTModel model) {
        for (NBTActionModel action : model.getActions()) {
            tag = innerPopulate(tag, action);
        }
        return tag;
    }

    @SneakyThrows
    private static CompoundNBT innerPopulate(CompoundNBT tag, NBTActionModel action) {
        if (action.getAction().equals("set")){
            return NBTUtils.setValue(tag, action.getKey(), JsonToNBT.getTagFromJson(action.getValue().toString()));
        }
        return tag;
    }

}
