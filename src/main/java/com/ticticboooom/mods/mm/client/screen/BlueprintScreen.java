package com.ticticboooom.mods.mm.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ticticboooom.mods.mm.Ref;
import com.ticticboooom.mods.mm.client.container.BlueprintContainer;
import com.ticticboooom.mods.mm.client.container.slot.BlueprintSlot;
import com.ticticboooom.mods.mm.client.helper.GLScissor;
import com.ticticboooom.mods.mm.client.helper.GuiBlockRenderBuilder;
import com.ticticboooom.mods.mm.data.DataRegistry;
import com.ticticboooom.mods.mm.data.model.ControllerModel;
import com.ticticboooom.mods.mm.data.model.StructureModel;
import com.ticticboooom.mods.mm.setup.MMRegistries;
import com.ticticboooom.mods.mm.structures.StructureKeyType;
import com.ticticboooom.mods.mm.util.GuiBlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlueprintScreen extends ContainerScreen<BlueprintContainer> {

    private static final ResourceLocation STRUCTURE_BG = new ResourceLocation(Ref.MOD_ID, "textures/gui/gui_large_jei.png");
    private final BlueprintContainer screenContainer;
    private float xRotation = 0;
    private double xLastMousePosition = 0;
    private float yRotation = 0;
    private double yLastMousePosition = 0;
    private int scrollLastPos = 0;
    private Vector3f prePos;

    private float controllerIndex = 0;
    private float scaleFactor = 1F;

    public BlueprintScreen(BlueprintContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.screenContainer = screenContainer;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Ref.MOD_ID, "textures/gui/blueprint_gui.png"));
        blit(matrixStack, guiLeft - 40, guiTop - 30, 0, 0, 256, 256);
        Minecraft.getInstance().textureManager.bindTexture(STRUCTURE_BG);
        blit(matrixStack, guiLeft + 40, guiTop - 15, 0, 0, 162, 150);
        Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(Ref.MOD_ID, "textures/gui/slot_parts.png"));
        for (int i = 0; i < 9; i++) {
            blit(matrixStack, guiLeft + 40 + (i * 18), guiTop + 110, 0, 26, 18, 18);
            blit(matrixStack, guiLeft + 40 + (i * 18), guiTop + 128, 0, 26, 18, 18);
        }

        GLScissor.enable(guiLeft + 40, guiTop - 15, 162, 120);

        matrixStack.push();
        matrixStack.translate(guiLeft, guiTop, 0);
        if (screenContainer.structure == null) {
            Optional<Map.Entry<ResourceLocation, StructureModel>> first = DataRegistry.STRUCTURES.entrySet().stream().findFirst();
            screenContainer.structure = first.get().getValue();
            renderStructure(screenContainer.structure, matrixStack, x, y, true);
            float tx = 9.75f, ty = -3, tz = 10;
            prePos = new Vector3f(tx, ty, tz);
            this.xRotation = -225;
            this.yRotation = 15;
            this.yLastMousePosition = 0;
            this.xLastMousePosition = 0;
            scaleFactor = 1;
        }
        if (screenContainer.structure != null) {
            renderStructure(screenContainer.structure, matrixStack, x, y, false);
        }
        matrixStack.pop();
        GLScissor.disable();
        renderItems();
    }

    private void renderStructure(StructureModel model, MatrixStack ms, int mouseX, int mouseY, boolean isInitial) {
        Quaternion rotation = new Quaternion(new Vector3f(1, 0, 0), yRotation, true);
        rotation.multiply(new Quaternion(new Vector3f(0, -1, 0), -xRotation, true));
        if (isInitial) {
            for (StructureModel.PositionedKey key : model.positionedKeys) {
                StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(key.type);
                value.onBlueprintInitialRender(key.pos, model, key.data);
            }
        } else {
            RenderSystem.enableBlend();
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableAlphaTest();
            RenderHelper.setupGui3DDiffuseLighting();
            RenderSystem.alphaFunc(516, 0.1F);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            List<GuiBlockRenderBuilder> blocks = new ArrayList<>();

            Minecraft mc = Minecraft.getInstance();
            if (GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_1) != 0 && GLFW.glfwGetKey(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == 0) {
                double relMoveX = mouseX - xLastMousePosition;
                double relMoveY = mouseY - yLastMousePosition;
                xRotation += relMoveX;
                yRotation += relMoveY;
            }
            if (GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) != 0 && GLFW.glfwGetKey(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == 0) {
                if (scrollLastPos == 0) {
                    scrollLastPos = (int) mouseY;
                }
                scaleFactor += (mouseY - yLastMousePosition) * 0.05;
                scaleFactor = Math.max(0.003f, scaleFactor);
            }
            if (GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != 0 && GLFW.glfwGetKey(mc.getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) != 0) {
                double relMoveX = mouseX - xLastMousePosition;
                double relMoveY = mouseY - yLastMousePosition;
                prePos.add((float) relMoveX * 0.08f, (float) -relMoveY * 0.08f, 0);
            }
            for (StructureModel.PositionedKey key : model.positionedKeys) {
                StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(key.type);
                blocks.add(value.onBlueprintRender(key.pos, model, key.data));

                for (GuiBlockRenderBuilder block : blocks) {
                    block.withPrePosition(prePos)
                            .withScale(new Vector3f(scaleFactor, scaleFactor, scaleFactor))
                            .withRotation(rotation)
                            .withScale(new Vector3f(1, -1, 1))
                            .finalize(ms);
                }
            }

            ResourceLocation cId = screenContainer.structure.controllerId.get((int) controllerIndex);
            controllerIndex += 0.01;
            if (controllerIndex >= screenContainer.structure.controllerId.size()) {
                controllerIndex = 0;
            }
            GuiBlockRenderBuilder guiBlockController = GuiBlockUtils.getGuiBlockController(new BlockPos(0, 0, 0), cId);
            guiBlockController.withPrePosition(prePos)
                    .withScale(new Vector3f(scaleFactor, scaleFactor, scaleFactor))
                    .withRotation(rotation)
                    .withScale(new Vector3f(1, -1, 1))
                    .finalize(ms);


            RenderSystem.disableAlphaTest();
            RenderSystem.disableRescaleNormal();
            // End tick
            xLastMousePosition = mouseX;
            yLastMousePosition = mouseY;
        }
    }

    private void renderItems() {
        List<ItemStack> items = new ArrayList<>();
        StructureModel model = screenContainer.structure;
        for (StructureModel.PositionedKey key : model.positionedKeys) {
            StructureKeyType value = MMRegistries.STRUCTURE_KEY_TYPES.getValue(key.type);
            ItemStack itemStack = value.onBlueprintListRender(model, key.data);
            ItemStack found = listContainsItem(items, itemStack);
            if (found != null) {
                found.setCount(found.getCount() + 1);
            } else {
                items.add(itemStack);
            }
        }
        for (Slot inventorySlot : screenContainer.inventorySlots) {
            BlueprintSlot slot = (BlueprintSlot) inventorySlot;
            int num = slot.index;
            if (num < items.size()){
                slot.setItem(items.get(num));
            } else {
                break;
            }
        }
    }

    private ItemStack listContainsItem(List<ItemStack> list, ItemStack stack) {
        for (ItemStack st : list) {
            if (st.equals(stack, false)) {
                return stack;
            }
        }
        return null;
    }
}
