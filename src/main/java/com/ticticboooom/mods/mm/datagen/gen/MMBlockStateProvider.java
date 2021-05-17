package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class MMBlockStateProvider extends BlockStateProvider {


    public MMBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MM.ID, exFileHelper);
    }

    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(MM.ID, "block/base_block");
    private static final ResourceLocation CONTROLLER_TEXTURE = new ResourceLocation(MM.ID, "block/controller_cutout");
    private static final ResourceLocation IPORT_TEXTURE = new ResourceLocation(MM.ID, "block/base_ports/item_input_cutout");
    private static final ResourceLocation OPORT_TEXTURE = new ResourceLocation(MM.ID, "block/base_ports/item_output_cutout");

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<ControllerBlock> controller : MMLoader.BLOCKS) {
            dynamicBlockNorthOverlay(controller.getId(), controller.get().getTexOverride() != null ? RLUtils.toRL(controller.get().getTexOverride()) : BASE_TEXTURE, CONTROLLER_TEXTURE);
            VariantBlockStateBuilder variantBuilder = getVariantBuilder(controller.get());
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(180).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(90).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(270).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.UP).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.DOWN).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
        }

        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : BASE_TEXTURE, IPORT_TEXTURE);
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : BASE_TEXTURE, OPORT_TEXTURE);
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }

        simpleBlock(MMSetup.PROJECTOR_BLOCK.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/projector")));
        simpleBlock(MMSetup.STRUCTURE_BLOCK.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/config_generator")));
    }

    public void dynamicBlockNorthOverlay(ResourceLocation loc, ResourceLocation baseTexture, ResourceLocation overlayTexture) {
        models().getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
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
        models().getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
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
                        .allFaces((dir, uv) -> uv.texture("#overlay"))
                        .end()
                )
                .end();
    }
}
