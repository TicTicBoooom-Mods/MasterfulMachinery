package com.ticticboooom.mods.mm.net.packets;

import com.ticticboooom.mods.mm.cap.Capabilities;
import com.ticticboooom.mods.mm.net.MMNetworkManager;
import com.ticticboooom.mods.mm.setup.MMItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class UpdateBluprintItemPacket {
    @AllArgsConstructor
    @Getter
    public static final class Data {
        private final UUID player;
        private final ResourceLocation structure;

        public static void encode(Data data, PacketBuffer buffer) {
            buffer.writeUniqueId(data.getPlayer());
            buffer.writeString(data.structure.toString());
        }

        public static Data decode(PacketBuffer buffer) {
            return new Data(buffer.readUniqueId(), ResourceLocation.tryCreate(buffer.readString()));
        }
    }

    public static void handle(Data data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ItemStack item = ctx.get().getSender().getHeldItemMainhand();
                if (item.getItem() == MMItems.BLUEPRINT.get()) {
                    item.getCapability(Capabilities.BLUEPRINT_DATA).resolve().get().setStructure(data.structure);
                    MMNetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), data);
                }
            } else {
                ClientWorld level = Minecraft.getInstance().world;
                for (AbstractClientPlayerEntity player : level.getPlayers()) {
                    if (player.getUniqueID().equals(data.player)) {
                        ItemStack item = player.getHeldItemMainhand();
                        if (item.getItem() == MMItems.BLUEPRINT.get()) {
                            item.getCapability(Capabilities.BLUEPRINT_DATA).resolve().get().setStructure(data.structure);
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
