package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.registration.MMLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class MMBlockStateProvider extends BlockStateProvider {


    public MMBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MM.ID, exFileHelper);
    }

    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(MM.ID, "blocks/base_block");
    private static final ResourceLocation CONTROLLER_TEXTURE = new ResourceLocation(MM.ID, "blocks/controller_cutout");
    private static final ResourceLocation IPORT_TEXTURE = new ResourceLocation(MM.ID, "blocks/port/input_cutout");
    private static final ResourceLocation OPORT_TEXTURE = new ResourceLocation(MM.ID, "blocks/port/output_cutout");

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<ControllerBlock> controller : MMLoader.BLOCKS) {
            dynamicBlockNorthOverlay(controller.getId(), BASE_TEXTURE, CONTROLLER_TEXTURE);
            simpleBlock(controller.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath())));
        }

        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            dynamicBlock(port.getId(), BASE_TEXTURE, IPORT_TEXTURE);
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            dynamicBlock(port.getId(), BASE_TEXTURE, OPORT_TEXTURE);
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }
    }

    public void dynamicBlockNorthOverlay(ResourceLocation loc, ResourceLocation baseTexture, ResourceLocation overlayTexture) {
        models().getBuilder(loc.getPath()).parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                .texture("particle", overlayTexture)
                .transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT)
                .rotation(75F, 45F, 0F)
                .translation(0F, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(75F, 45F, 0F)
                .translation(0F, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .end()
                .customLoader(MultiLayerModelBuilder::begin)
                .submodel(RenderType.solid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("base", baseTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .cube("#base")
                        //.allFaces((dir, uv) -> uv.uvs(0F,0.0F, 16F,16F))
                        .end()
                )
                .submodel(RenderType.translucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("overlay", overlayTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .face(Direction.NORTH)
                            .texture("#overlay")
                            //.allFaces((dir, uv) -> uv.uvs(0F,0F, 16F,16F))
                            .end()
                        .end()
                )
                .end();
    }
    public void dynamicBlock(ResourceLocation loc, ResourceLocation baseTexture, ResourceLocation overlayTexture) {
        models().getBuilder(loc.getPath()).parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                .texture("particle", overlayTexture)
                .transforms()
                .transform(ModelBuilder.Perspective.THIRDPERSON_LEFT)
                .rotation(75F, 45F, 0F)
                .translation(0F, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
                .rotation(75F, 45F, 0F)
                .translation(0F, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .end()
                .customLoader(MultiLayerModelBuilder::begin)
                .submodel(RenderType.solid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("base", baseTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .cube("#base")
                        //.allFaces((dir, uv) -> uv.uvs(0F,0.0F, 16F,16F))
                        .end()
                )
                .submodel(RenderType.translucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("overlay", overlayTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .cube("#overlay")
                        .end()
                )
                .end();
    }
}
