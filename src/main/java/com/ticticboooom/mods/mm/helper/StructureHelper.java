package com.ticticboooom.mods.mm.helper;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureHelper {
    static String allChars;

    static {
        int[] codePoints = new int[0x110000];
        for (int i = 0; i < codePoints.length; i++) {

            codePoints[i] = i;
        }

        allChars = new String(codePoints, 0, codePoints.length);
    }

    private static Gson GSON = null;

    public static void copyToClickBoard(CompoundNBT tag, World world) {
        if (tag == null) {
            return;
        }

        if (!tag.contains("pos1") || !tag.contains("pos2")) {
            return;
        }

        BlockPos pos1 = NBTHelper.fromCompound(tag.getCompound("pos1"));
        BlockPos pos2 = NBTHelper.fromCompound(tag.getCompound("pos2"));

        int x = Math.min(pos1.getX(), pos2.getX());
        int y = Math.min(pos1.getY(), pos2.getY());
        int z = Math.min(pos1.getZ(), pos2.getZ());
        int dx = Math.max(pos1.getX(), pos2.getX());
        int dy = Math.max(pos1.getY(), pos2.getY());
        int dz = Math.max(pos1.getZ(), pos2.getZ());

        BlockPos minPos = new BlockPos(x, y, z);
        BlockPos maxPos = new BlockPos(dx, dy, dz);

        List<List<String>> layout = new ArrayList<>();
        Map<Character, ResourceLocation> legend = new HashMap<>();
        Map<ResourceLocation, Character> legendLookup = new HashMap<>();

        char index = 41;

        for (int yi = minPos.getY(); yi <= maxPos.getY(); yi++) {
            ArrayList<String> strings = new ArrayList<>();
            for (int zi = minPos.getZ(); zi <= maxPos.getZ(); zi++) {
                StringBuilder row = new StringBuilder();
                for (int xi = minPos.getX(); xi <= maxPos.getX(); xi++) {
                    BlockState state = world.getBlockState(new BlockPos(xi, yi, zi));
                    ResourceLocation registryName = state.getBlock().getRegistryName();

                    if (index == 'C'){
                        index++;
                    }

                    if (state.isAir()) {
                        row.append(" ");
                        continue;
                    }

                    if (state.getBlock() instanceof ControllerBlock) {
                        row.append('C');
                        continue;
                    }

                    if (!legendLookup.containsKey(registryName)) {
                        legend.put(index, registryName);
                        legendLookup.put(registryName, index);
                        index++;
                    }
                    row.append(legendLookup.get(registryName));
                }
                strings.add(row.toString());
            }
            layout.add(strings);
        }

        JsonObject jsonObject = toJson(legend, layout);
        String s = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create().toJson(jsonObject);
        Minecraft.getInstance().keyboardListener.setClipboardString(s);
    }

    private static JsonObject toJson(Map<Character, ResourceLocation> legend, List<List<String>> layout) {
        JsonObject result = new JsonObject();
        result.addProperty("type", "masterfulmachinery:machine_structure");
        result.addProperty("id", "change_this");
        result.addProperty("controllerId", "change_this");
        result.addProperty("name", "Change This");

        DataResult<JsonElement> apply = JsonOps.INSTANCE.withEncoder(Codec.list(Codec.list(Codec.STRING))).apply(layout);
        JsonArray layoutJson = apply.result().get().getAsJsonArray();

        result.add("layout", layoutJson);
        JsonObject legendJson = new JsonObject();
        for (Map.Entry<Character, ResourceLocation> entry : legend.entrySet()) {
            JsonObject inner = new JsonObject();
            inner.addProperty("block", entry.getValue().toString());
            legendJson.add(entry.getKey().toString(), inner);
        }

        result.add("legend", legendJson);

        return result;
    }
}
