package com.ticticboooom.mods.mm.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.nbt.model.NBTActionModel;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;
import net.minecraft.data.NBTToSNBTConverter;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class NBTActionParser {
    public static NBTModel parse(JsonArray section) {
        List<NBTActionModel> resultList = new ArrayList<>();
        for (JsonElement elem : section) {
            JsonObject obj = elem.getAsJsonObject();
            NBTActionModel model = new NBTActionModel(
                    obj.get("action").getAsString(),
                    obj.get("key").getAsString(),
                    obj.get("value")
            );
            resultList.add(model);
        }
        if (resultList.isEmpty()){
            return null;
        }
        return new NBTModel(resultList);
    }

    public static NBTModel parse(PacketBuffer buf) {
        int i = buf.readInt();
        List<NBTActionModel> resultList = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            resultList.add(new NBTActionModel(buf.readString(), buf.readString(), new JsonParser().parse(buf.readString())));
        }
        if (i <= 0){
            return null;
        }
        return new NBTModel(resultList);
    }

    public static void write(PacketBuffer buf, NBTModel model) {
        buf.writeInt(model.getActions().size());
        for (NBTActionModel action : model.getActions()) {
            buf.writeString(action.getAction());
            buf.writeString(action.getKey());
            buf.writeString(action.getValue().toString());
        }
    }
}
