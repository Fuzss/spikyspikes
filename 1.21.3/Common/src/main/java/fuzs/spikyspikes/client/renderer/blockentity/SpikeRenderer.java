package fuzs.spikyspikes.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.spikyspikes.client.model.geom.ModelPartCopy;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Map;
import java.util.function.BiConsumer;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private static final Map<SpikeMaterial, Material> MATERIAL_BY_TYPE;

    static {
        ImmutableMap.Builder<SpikeMaterial, Material> builder = ImmutableMap.builder();
        registerMaterial(SpikeMaterial.WOOD,
                ResourceLocationHelper.withDefaultNamespace("block/stripped_oak_log"),
                builder::put);
        registerMaterial(SpikeMaterial.STONE,
                ResourceLocationHelper.withDefaultNamespace("block/smooth_stone"),
                builder::put);
        registerMaterial(SpikeMaterial.IRON,
                ResourceLocationHelper.withDefaultNamespace("block/iron_block"),
                builder::put);
        registerMaterial(SpikeMaterial.GOLD,
                ResourceLocationHelper.withDefaultNamespace("block/gold_block"),
                builder::put);
        registerMaterial(SpikeMaterial.DIAMOND,
                ResourceLocationHelper.withDefaultNamespace("block/diamond_block"),
                builder::put);
        registerMaterial(SpikeMaterial.NETHERITE,
                ResourceLocationHelper.withDefaultNamespace("block/netherite_block"),
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

    public SpikeRenderer(BlockEntityRendererProvider.Context context) {
        // NO-OP
    }

    private static void registerMaterial(SpikeMaterial spikeMaterial, ResourceLocation resourceLocation, BiConsumer<SpikeMaterial, Material> registrar) {
        registrar.accept(spikeMaterial, new Material(InventoryMenu.BLOCK_ATLAS, resourceLocation));
    }

    @Override
    public void render(SpikeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
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
    }

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
