package fuzs.spikyspikes.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.client.renderer.blockentity.state.SpikeRenderState;
import fuzs.spikyspikes.world.level.block.EnchantmentGlintBlock;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * @see BlockRenderDispatcher#renderSingleBlock(BlockState, PoseStack, MultiBufferSource, int, int)
 */
public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity, SpikeRenderState> {
    private final BlockRenderDispatcher blockRenderer;
    private final BlockColors blockColors;

    public SpikeRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.blockRenderDispatcher();
        this.blockColors = Minecraft.getInstance().getBlockColors();
    }

    @Override
    public SpikeRenderState createRenderState() {
        return new SpikeRenderState();
    }

    @Override
    public void extractRenderState(SpikeBlockEntity blockEntity, SpikeRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity,
                renderState,
                partialTick,
                cameraPosition,
                crumblingOverlay);
        renderState.blockColor = this.blockColors.getColor(renderState.blockState,
                blockEntity.getLevel(),
                blockEntity.getBlockPos(),
                0);
    }

    @Override
    public void submit(SpikeRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        // When the spike is enchanted, we set the render shape to invisible and handle everything via this block entity renderer.
        // Overlaying the glint on the block model baked into chunk geometry has issues with z-fighting which couldn't be solved.
        // Having the render shape as invisible has issues though and hides breaking particles and when rendering in a minecart, held by enderman, etc.
        if (renderState.blockState.getBlock() instanceof EnchantmentGlintBlock block
                && block.hasFoil(renderState.blockState)) {
            submitNodeCollector.order(1)
                    .submitCustomGeometry(poseStack,
                            ItemBlockRenderTypes.getMovingBlockRenderType(renderState.blockState),
                            (PoseStack.Pose pose, VertexConsumer vertexConsumer) -> {
                                this.renderSpike(pose,
                                        vertexConsumer,
                                        this.blockRenderer.getBlockModel(renderState.blockState),
                                        renderState.lightCoords,
                                        renderState.blockColor);
                            });
            submitNodeCollector.order(2)
                    .submitCustomGeometry(poseStack,
                            getFoilRenderType(renderState.blockState),
                            (PoseStack.Pose pose, VertexConsumer vertexConsumer) -> {
                                this.renderSpike(pose,
                                        vertexConsumer,
                                        this.blockRenderer.getBlockModel(renderState.blockState),
                                        renderState.lightCoords,
                                        renderState.blockColor);
                            });
            if (renderState.breakProgress != null) {
                submitNodeCollector.order(3)
                        .submitCustomGeometry(poseStack,
                                ModelBakery.DESTROY_TYPES.get(renderState.breakProgress.progress()),
                                (PoseStack.Pose pose, VertexConsumer vertexConsumer) -> {
                                    VertexConsumer textureGenerator = new SheetedDecalTextureGenerator(vertexConsumer,
                                            renderState.breakProgress.cameraPose(),
                                            1.0F);
                                    this.renderSpike(pose,
                                            textureGenerator,
                                            this.blockRenderer.getBlockModel(renderState.blockState),
                                            renderState.lightCoords,
                                            renderState.blockColor);
                                });
            }
        }
    }

    private void renderSpike(PoseStack.Pose pose, VertexConsumer vertexConsumer, BlockStateModel blockStateModel, int packedLight, int color) {
        ModelBlockRenderer.renderModel(pose,
                vertexConsumer,
                blockStateModel,
                ARGB.redFloat(color),
                ARGB.greenFloat(color),
                ARGB.blueFloat(color),
                packedLight,
                OverlayTexture.NO_OVERLAY);
    }

    /**
     * Has to be {@link ItemBlockRenderTypes#getMovingBlockRenderType(BlockState)}, not
     * {@link ItemBlockRenderTypes#getRenderType(BlockState)} for the render type otherwise used for the block model.
     */
    public static RenderType getFoilRenderType(BlockState blockState) {
        return getFoilRenderType(ItemBlockRenderTypes.getMovingBlockRenderType(blockState));
    }

    public static RenderType getFoilRenderType(RenderType renderType) {
        // just some quirky getter for avoiding the private vanilla method
        return ItemRenderer.getFoilRenderTypes(renderType, true, true).get(1);
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
