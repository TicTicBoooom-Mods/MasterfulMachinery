package com.ticticboooom.mods.mm.datagen.gen.runtime;

import com.ticticboooom.mods.mm.MM;
import com.ticticboooom.mods.mm.block.ControllerBlock;
import com.ticticboooom.mods.mm.block.MachinePortBlock;
import com.ticticboooom.mods.mm.helper.RLUtils;
import com.ticticboooom.mods.mm.ports.MasterfulPortType;
import com.ticticboooom.mods.mm.registration.MMLoader;
import com.ticticboooom.mods.mm.registration.MMPorts;
import com.ticticboooom.mods.mm.registration.MMSetup;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.MultiLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class MMBlockStateProvider extends BlockStateProvider {


    public MMBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MM.ID, exFileHelper);
    }
    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation(MM.ID, "block/base_block");
    private static final ResourceLocation CONTROLLER_TEXTURE = new ResourceLocation(MM.ID, "block/controller_cutout");

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<ControllerBlock> controller : MMLoader.BLOCKS) {
            if (!controller.isPresent()) {
                return;
            }
            dynamicBlockNorthOverlay(controller.getId(), controller.get().getTexOverride() != null ? RLUtils.toRL(controller.get().getTexOverride()) : BASE_TEXTURE, CONTROLLER_TEXTURE);
            VariantBlockStateBuilder variantBuilder = getVariantBuilder(controller.get());
            variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(0).addModel();
            variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(180).addModel();
            variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(90).addModel();
            variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + controller.getId().getPath()))).rotationY(270).addModel();
        }

        for (RegistryObject<MachinePortBlock> port : MMLoader.IPORT_BLOCKS) {
            MasterfulPortType masterfulPortType = MMPorts.PORTS.get(port.get().getPortTypeId());
            masterfulPortType.getParser().generateBlockStates(this, true, port);
        }
        for (RegistryObject<MachinePortBlock> port : MMLoader.OPORT_BLOCKS) {
            MasterfulPortType masterfulPortType = MMPorts.PORTS.get(port.get().getPortTypeId());
            masterfulPortType.getParser().generateBlockStates(this, false, port);
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


    private void directionalState(Block block) {
        VariantBlockStateBuilder variantBuilder = getVariantBuilder(block);
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(0).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.SOUTH).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(180).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.EAST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(90).addModel();
        variantBuilder.partialState().with(HorizontalBlock.HORIZONTAL_FACING, Direction.WEST).modelForState().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(MM.ID, "block/" + block.getRegistryName().getPath()))).rotationY(270).addModel();

    }
}
