package fuzs.spikyspikes.client.renderer.blockentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Map;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private static final Map<SpikeBlock.SpikeMaterial, Material> MATERIAL_BY_TYPE = Util.make(Maps.<SpikeBlock.SpikeMaterial, ResourceLocation>newHashMap(), (p_112552_) -> {
        p_112552_.put(SpikeBlock.SpikeMaterial.WOOD, new ResourceLocation("block/oak_planks"));
        p_112552_.put(SpikeBlock.SpikeMaterial.STONE, new ResourceLocation("block/cobblestone"));
        p_112552_.put(SpikeBlock.SpikeMaterial.IRON, new ResourceLocation("block/iron_block"));
        p_112552_.put(SpikeBlock.SpikeMaterial.GOLD, new ResourceLocation("block/gold_block"));
        p_112552_.put(SpikeBlock.SpikeMaterial.DIAMOND, new ResourceLocation("block/diamond_block"));
        p_112552_.put(SpikeBlock.SpikeMaterial.NETHERITE, new ResourceLocation("block/netherite_block"));
    }).entrySet().stream().collect(Maps.toImmutableEnumMap(Map.Entry::getKey, entry -> new Material(InventoryMenu.BLOCK_ATLAS, entry.getValue())));

    private final ModelPart model = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, false);
    private final ModelPart inventoryModel = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, true);

    @Override
    public void render(SpikeBlockEntity p_112307_, float p_112308_, PoseStack poseStack, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        poseStack.pushPose();
        Direction direction = p_112307_.getBlockState().getValue(SpikeBlock.FACING);
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getOpposite().getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = MATERIAL_BY_TYPE.get(((SpikeBlock) p_112307_.getBlockState().getBlock()).spikeMaterial);
        VertexConsumer vertexconsumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(p_112310_, material.renderType(RenderType::entitySolid), true, false));
        ModelPart model = p_112307_.inventoryRendering ? this.inventoryModel : this.model;
        model.render(poseStack, vertexconsumer, p_112311_, p_112312_);
        poseStack.popPose();
    }
}
