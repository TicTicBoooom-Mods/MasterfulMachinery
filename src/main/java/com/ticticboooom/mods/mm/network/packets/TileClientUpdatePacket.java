package com.ticticboooom.mods.mm.network.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TileClientUpdatePacket {
    @AllArgsConstructor
    @Getter
    public static final class Data {
        private final BlockPos pos;
        private final CompoundNBT nbt;

        public static void encode(Data data, PacketBuffer buffer){
            buffer.writeBlockPos(data.getPos());
            buffer.writeNbt(data.nbt);
        }

        public static Data decode(PacketBuffer buffer){
            return new Data(buffer.readBlockPos(), buffer.readNbt());
        }
    }

    public static void handle(Data data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld level = Minecraft.getInstance().level;
            TileEntity blockEntity = level.getBlockEntity(data.getPos());
            BlockState state = level.getBlockState(data.getPos());
            if (blockEntity != null) {
                blockEntity.load(state, data.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
