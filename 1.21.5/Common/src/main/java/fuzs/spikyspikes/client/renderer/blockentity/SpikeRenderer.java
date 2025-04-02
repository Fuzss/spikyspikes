package fuzs.spikyspikes.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SpecialBlockModelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;
    private final BlockColors blockColors;

    public SpikeRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.blockColors = Minecraft.getInstance().getBlockColors();
    }

    @Override
    public void render(SpikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, Vec3 cameraPosition) {
        this.renderSingleBlock(blockEntity.getBlockState(), poseStack, multiBufferSource, packedLight, packedOverlay);
    }

    /**
     * Copied from {@link BlockRenderDispatcher#renderSingleBlock(BlockState, PoseStack, MultiBufferSource, int, int)},
     * allows for showing the enchantment glint.
     */
    void renderSingleBlock(BlockState blockState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        RenderShape renderShape = blockState.getRenderShape();
        if (renderShape == RenderShape.INVISIBLE) {
            BlockStateModel blockStateModel = this.blockRenderer.getBlockModel(blockState);
            int i = this.blockColors.getColor(blockState, null, null, 0);
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float g = (float) (i >> 8 & 0xFF) / 255.0F;
            float h = (float) (i & 0xFF) / 255.0F;
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource,
                    ItemBlockRenderTypes.getRenderType(blockState),
                    true,
                    blockState.getOptionalValue(SpikeBlock.ENCHANTED).orElse(false));
            ModelBlockRenderer.renderModel(poseStack.last(),
                    vertexConsumer,
                    blockStateModel,
                    f,
                    g,
                    h,
                    packedLight,
                    packedOverlay);
            SpecialBlockModelRenderer specialBlockModelRenderer = this.blockRenderer.getBlockModelShaper()
                    .getModelManager()
                    .specialBlockModelRenderer()
                    .get();
            specialBlockModelRenderer.renderByBlock(blockState.getBlock(),
                    ItemDisplayContext.NONE,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay);
        }
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
