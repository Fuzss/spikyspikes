package fuzs.spikyspikes.client.renderer.blockentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.client.model.geom.PureModelPart;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.HashMap;
import java.util.Map;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private static final Map<SpikeMaterial, Material> MATERIAL_BY_TYPE = new HashMap<SpikeMaterial, ResourceLocation>() {{
        this.put(SpikeMaterial.WOOD, new ResourceLocation("block/stripped_oak_log"));
        this.put(SpikeMaterial.STONE, new ResourceLocation("block/smooth_stone"));
        this.put(SpikeMaterial.IRON, new ResourceLocation("block/iron_block"));
        this.put(SpikeMaterial.GOLD, new ResourceLocation("block/gold_block"));
        this.put(SpikeMaterial.DIAMOND, new ResourceLocation("block/diamond_block"));
        this.put(SpikeMaterial.NETHERITE, new ResourceLocation("block/netherite_block"));
    }}.entrySet().stream().collect(Maps.toImmutableEnumMap(Map.Entry::getKey, entry -> new Material(InventoryMenu.BLOCK_ATLAS, entry.getValue())));

    private static final PureModelPart MODEL = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, false);
    private static final PureModelPart INVENTORY_MODEL = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, true);

    @Override
    public void render(SpikeBlockEntity p_112307_, float p_112308_, PoseStack poseStack, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        Direction direction = p_112307_.getBlockState().getValue(SpikeBlock.FACING);
        SpikeMaterial spikeMaterial = ((SpikeBlock) p_112307_.getBlockState().getBlock()).spikeMaterial;
        renderSpike(direction, spikeMaterial, poseStack, p_112310_, p_112311_, p_112312_, false, p_112307_.hasFoil());
    }

    public static void renderSpike(Direction direction, SpikeMaterial spikeMaterial, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int overlay, boolean inventoryRendering, boolean withFoil) {
        // from shulker box renderer
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getOpposite().getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = MATERIAL_BY_TYPE.get(spikeMaterial);
        VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(bufferSource, material.renderType(RenderType::entitySolid), true, withFoil));
        PureModelPart model = inventoryRendering ? INVENTORY_MODEL : MODEL;
        model.render(poseStack, vertexconsumer, combinedLight, overlay);
        poseStack.popPose();
    }
}
