package fuzs.spikyspikes.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.spikyspikes.client.model.geom.ModelPartCopy;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SpecialBlockModelRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.BiConsumer;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private static final Map<SpikeMaterial, Material> MATERIAL_BY_TYPE;

    static {
        ImmutableMap.Builder<SpikeMaterial, Material> builder = ImmutableMap.builder();
        registerMaterial(SpikeMaterial.WOOD,
                ModelLocationHelper.getBlockTexture(Blocks.STRIPPED_OAK_LOG),
                builder::put);
        registerMaterial(SpikeMaterial.STONE, ModelLocationHelper.getBlockTexture(Blocks.SMOOTH_STONE), builder::put);
        registerMaterial(SpikeMaterial.IRON, ModelLocationHelper.getBlockTexture(Blocks.IRON_BLOCK), builder::put);
        registerMaterial(SpikeMaterial.GOLD, ModelLocationHelper.getBlockTexture(Blocks.GOLD_BLOCK), builder::put);
        registerMaterial(SpikeMaterial.DIAMOND,
                ModelLocationHelper.getBlockTexture(Blocks.DIAMOND_BLOCK),
                builder::put);
        registerMaterial(SpikeMaterial.NETHERITE,
                ModelLocationHelper.getBlockTexture(Blocks.NETHERITE_BLOCK),
                builder::put);
        MATERIAL_BY_TYPE = builder.build();
    }

    private static final ModelPartCopy MODEL = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, false);
    private static final ModelPartCopy INVENTORY_MODEL = ShapeModelPart.pyramid(-8.0F,
            -8.0F,
            -8.0F,
            16.0F,
            16.0F,
            16.0F,
            true);

    private final BlockRenderDispatcher blockRenderer;
    private final BlockColors blockColors;

    public SpikeRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.blockColors = Minecraft.getInstance().getBlockColors();
    }

    private static void registerMaterial(SpikeMaterial spikeMaterial, ResourceLocation resourceLocation, BiConsumer<SpikeMaterial, Material> registrar) {
        registrar.accept(spikeMaterial, new Material(TextureAtlas.LOCATION_BLOCKS, resourceLocation));
    }

    @Override
    public void render(SpikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        // TODO remove this check once the custom block model is supported for NeoForge
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            Direction direction = blockEntity.getBlockState().getValue(SpikeBlock.FACING);
            SpikeMaterial spikeMaterial = ((SpikeBlock) blockEntity.getBlockState().getBlock()).getSpikeMaterial();
            renderSpike(direction,
                    spikeMaterial,
                    poseStack,
                    multiBufferSource,
                    packedLight,
                    packedOverlay,
                    false,
                    blockEntity.hasFoil());
        } else {
            this.renderSingleBlock(blockEntity.getBlockState(),
                    poseStack,
                    multiBufferSource,
                    packedLight,
                    packedOverlay);
        }
    }

    /**
     * Copied from {@link BlockRenderDispatcher#renderSingleBlock(BlockState, PoseStack, MultiBufferSource, int, int)},
     * allows for showing the enchantment glint.
     */
    public void renderSingleBlock(BlockState blockState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        RenderShape renderShape = blockState.getRenderShape();
        if (renderShape == RenderShape.INVISIBLE) {
            BakedModel bakedModel = this.blockRenderer.getBlockModel(blockState);
            int i = this.blockColors.getColor(blockState, null, null, 0);
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float g = (float) (i >> 8 & 0xFF) / 255.0F;
            float h = (float) (i & 0xFF) / 255.0F;
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource,
                    ItemBlockRenderTypes.getRenderType(blockState),
                    true,
                    blockState.getOptionalValue(SpikeBlock.ENCHANTED).orElse(false));
            this.blockRenderer.getModelRenderer()
                    .renderModel(poseStack.last(),
                            vertexConsumer,
                            blockState,
                            bakedModel,
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

    @Deprecated
    public static void renderSpike(Direction direction, SpikeMaterial spikeMaterial, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean inventoryRendering, boolean isFoil) {
        // from shulker box renderer
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getOpposite().getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = MATERIAL_BY_TYPE.get(spikeMaterial);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource,
                material.renderType(RenderType::entitySolid),
                true,
                isFoil);
        ModelPartCopy model = inventoryRendering ? INVENTORY_MODEL : MODEL;
        model.render(poseStack, material.sprite().wrap(vertexConsumer), packedLight, packedOverlay);
        poseStack.popPose();
    }
}
