package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;

public class MMLangProvider extends LanguageProvider {
    public MMLangProvider(DataGenerator gen) {
        super(gen, MM.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (RegistryObject<ControllerBlock> block : MMLoader.BLOCKS) {
            this.add(block.get(), block.get().getControllerName() + " Controller");
            this.add("container.masterfulmachinery." + block.get().getControllerId() + "_controller.name", block.get().getControllerName() + " Controller");
            for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
                if (port.get().getControllerId().equals(block.get().getControllerId())){
                    this.add(port.get(), block.get().getControllerName() + " - " + port.get().getLangName() + " Input Port");
                }
            }

            for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
                if (port.get().getControllerId().equals(block.get().getControllerId())) {
                    this.add(port.get(), block.get().getControllerName() + " - " + port.get().getLangName() + " Output Port");
                }
            }

            this.add(MMSetup.BLUEPRINT.get(), "Blueprint");
        }
    }
}
