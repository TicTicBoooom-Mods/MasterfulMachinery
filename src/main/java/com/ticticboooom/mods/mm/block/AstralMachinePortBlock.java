package com.ticticboooom.mods.mm.block;

import com.ticticboooom.mods.mm.block.tile.MachinePortBlockEntity;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightRecipient;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightNetworkRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.Random;

public class AstralMachinePortBlock extends MachinePortBlock implements BlockStarlightRecipient {
    public AstralMachinePortBlock(RegistryObject<TileEntityType<?>> type, String name, String controllerId, String textureOverride, ResourceLocation overlay) {
        super(type, name, controllerId, textureOverride, overlay);
    }

    @Override
    public void receiveStarlight(World world, Random random, BlockPos blockPos, IWeakConstellation iWeakConstellation, double v) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof MachinePortBlockEntity){
            PortStorage storage = ((MachinePortBlockEntity) tile).getStorage();
            if (!((MachinePortBlockEntity) tile).isInput()) {
                return;
            }

            if (storage instanceof StarlightPortStorage) {
                StarlightPortStorage starlightStorage = (StarlightPortStorage) storage;
                int rec = new Double(v).intValue();
                starlightStorage.getInv().receiveStarlight(rec, false);
            }
        }
    }
}
