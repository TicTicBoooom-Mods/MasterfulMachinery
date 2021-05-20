package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class MMItemModelProvider extends ItemModelProvider {


    public MMItemModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MM.ID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<ControllerBlock> controller : MMLoader.BLOCKS) {
            this.getBuilder(controller.getId().toString()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(controller.getId().getNamespace(), "block/" + controller.getId().getPath())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            this.getBuilder(port.getId().toString()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(port.getId().getNamespace(), "block/" + port.getId().getPath())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            this.getBuilder(port.getId().toString()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(port.getId().getNamespace(), "block/" + port.getId().getPath())));
        }

        this.getBuilder(MMSetup.BLUEPRINT.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/blueprint");
        this.getBuilder(MMSetup.STRUCTURE_DEVICE.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/scanning_tool");
        this.getBuilder(MMSetup.STRUCTURE_ITEM.getId().getPath()).parent(new ModelFile.UncheckedModelFile(RLUtils.toRL(MM.ID + ":block/structure_generator")));
        this.getBuilder(MMSetup.PROJECTOR_ITEM.getId().getPath()).parent(new ModelFile.UncheckedModelFile(RLUtils.toRL(MM.ID + ":block/" + MMSetup.PROJECTOR_BLOCK.getId().getPath())));
    }
}