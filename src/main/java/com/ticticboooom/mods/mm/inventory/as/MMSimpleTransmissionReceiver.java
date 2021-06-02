package com.ticticboooom.mods.mm.inventory.as;

import com.ticticboooom.mods.mm.block.tile.AstralMachineInputPortBlockEntity;
import com.ticticboooom.mods.mm.ports.storage.PortStorage;
import com.ticticboooom.mods.mm.ports.storage.StarlightPortStorage;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MMSimpleTransmissionReceiver extends SimpleTransmissionReceiver<AstralMachineInputPortBlockEntity> {


    private AstralMachineInputPortBlockEntity astralMachineInputPortBlockEntity;

    public MMSimpleTransmissionReceiver(BlockPos thisPos) {
        super(thisPos);
    }

    @Override
    public boolean syncTileData(World world, AstralMachineInputPortBlockEntity astralMachineInputPortBlockEntity) {
        this.astralMachineInputPortBlockEntity = astralMachineInputPortBlockEntity;
        return true;
    }

    @Override
    public Class<AstralMachineInputPortBlockEntity> getTileClass() {
        return AstralMachineInputPortBlockEntity.class;
    }

    @Override
    public void onStarlightReceive(World world, IWeakConstellation iWeakConstellation, double v) {
        AstralMachineInputPortBlockEntity tileAtPos = this.getTileAtPos(world);
        if (tileAtPos == null){
            return;
        }
        PortStorage storage = tileAtPos.getStorage();
        if (storage instanceof StarlightPortStorage) {
            ((StarlightPortStorage) storage).getInv().receiveStarlight((int)v, false);
        }
    }

    @Override
    public TransmissionProvider getProvider() {
        return new Provider();
    }

    public static final class Provider extends TransmissionProvider {

        @Override
        public IPrismTransmissionNode get() {
            return new MMSimpleTransmissionReceiver(null);
        }
    }
}
