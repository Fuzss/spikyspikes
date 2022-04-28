package fuzs.spikyspikes.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    private final ModelPart model = ShapeModelPart.pyramid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
    private final Material material;

    public SpikeRenderer(ResourceLocation textureLocation) {
        this.material = new Material(InventoryMenu.BLOCK_ATLAS, textureLocation);
    }

    @Override
    public void render(SpikeBlockEntity p_112307_, float p_112308_, PoseStack poseStack, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        poseStack.pushPose();
        Direction direction = p_112307_.getBlockState().getValue(SpikeBlock.FACING);
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getOpposite().getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer vertexconsumer = this.material.sprite().wrap(ItemRenderer.getFoilBufferDirect(p_112310_, this.material.renderType(RenderType::entitySolid), true, false));;
        this.model.render(poseStack, vertexconsumer, p_112311_, p_112312_);
        poseStack.popPose();
    }
}
