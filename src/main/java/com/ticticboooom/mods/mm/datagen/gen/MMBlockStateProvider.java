package com.ticticboooom.mods.mm.datagen.gen;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.model.ModelOverrideModel;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalBlock;
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
            if (controller.get().getModelOverride() != null) {
                ModelOverrideModel modelOverride = controller.get().getModelOverride();
                if (modelOverride.getCommonModel().isPresent()) {
                    ResourceLocation location = RLUtils.toRL(modelOverride.getCommonModel().get());
                    dynamicBlockNorthOverlayModel(controller.getId(), location, CONTROLLER_TEXTURE);
                }
            } else {
                dynamicBlockNorthOverlay(controller.getId(), controller.get().getTexOverride() != null ? RLUtils.toRL(controller.get().getTexOverride()) : BASE_TEXTURE, CONTROLLER_TEXTURE);
            }
            VariantBlockStateBuilder variantBuilder = getVariantBuilder(controller.get());
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(180).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(90).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(270).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.UP).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
            variantBuilder.partialState().with(DirectionalBlock.FACING, Direction.DOWN).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
        }

        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            if (port.get().getModelOverride() != null) {
                ModelOverrideModel modelOverride = port.get().getModelOverride();
                if (modelOverride.getInputModel().isPresent()) {
                    dynamicBlockModel(port.getId(), RLUtils.toRL(modelOverride.getInputModel().get()), port.get().getOverlay());
                } else if (modelOverride.getCommonModel().isPresent()) {
                    dynamicBlockModel(port.getId(), RLUtils.toRL(modelOverride.getCommonModel().get()), port.get().getOverlay());
                }
            } else {
                dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : BASE_TEXTURE, port.get().getOverlay());
            }
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            if (port.get().getModelOverride() != null) {
                ModelOverrideModel modelOverride = port.get().getModelOverride();
                if (modelOverride.getOutputModel().isPresent()) {
                    dynamicBlockModel(port.getId(), RLUtils.toRL(modelOverride.getOutputModel().get()), port.get().getOverlay());
                } else if (modelOverride.getCommonModel().isPresent()) {
                    dynamicBlockModel(port.getId(), RLUtils.toRL(modelOverride.getCommonModel().get()), port.get().getOverlay());
                }
            } else {
                dynamicBlock(port.getId(), port.get().getTextureOverride() != null ? RLUtils.toRL(port.get().getTextureOverride()) : BASE_TEXTURE, port.get().getOverlay());
            }
            simpleBlock(port.get(), new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + port.getId().getPath())));
        }

        directionalState(MMSetup.PROJECTOR_BLOCK.get());

        directionalState(MMSetup.STRUCTURE_BLOCK.get());
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
                .submodel(RenderType.getSolid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("base", baseTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .cube("#base")
                        //.allFaces((dir, uv) -> uv.uvs(0F,0.0F, 16F,16F))
                        .end()
                )
                .submodel(RenderType.getTranslucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
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
                .submodel(RenderType.getSolid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("base", baseTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .cube("#base")
                        //.allFaces((dir, uv) -> uv.uvs(0F,0.0F, 16F,16F))
                        .end()
                )
                .submodel(RenderType.getTranslucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("overlay", overlayTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .allFaces((dir, uv) -> uv.texture("#overlay"))
                        .end()
                )
                .end();
    }

    public void dynamicBlockModel(ResourceLocation loc, ResourceLocation baseModel, ResourceLocation overlayTexture) {
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
                .submodel(RenderType.getSolid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(baseModel)))
                .submodel(RenderType.getTranslucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                        .texture("overlay", overlayTexture)
                        .element()
                        .from(0, 0, 0)
                        .to(16, 16, 16)
                        .allFaces((dir, uv) -> uv.texture("#overlay"))
                        .end()
                )
                .end();
    }
    public void dynamicBlockNorthOverlayModel(ResourceLocation loc, ResourceLocation baseModel, ResourceLocation overlayTexture) {
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
                .submodel(RenderType.getSolid(), this.models().nested().parent(new ModelFile.UncheckedModelFile(baseModel)))
                .submodel(RenderType.getTranslucent(), this.models().nested().parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
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

    private void directionalState(Block block) {
        VariantBlockStateBuilder variantBuilder = getVariantBuilder(block);
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(0).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(180).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(90).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(270).addModel();

    }
}
