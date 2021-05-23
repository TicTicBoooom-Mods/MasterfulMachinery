package com.ticticboooom.mods.mm.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ticticboooom.mods.mm.nbt.model.NBTActionModel;
import com.ticticboooom.mods.mm.nbt.model.NBTModel;

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
        return new NBTModel(resultList);
    }
}
