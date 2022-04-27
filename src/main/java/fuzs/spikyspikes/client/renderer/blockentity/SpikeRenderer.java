package fuzs.spikyspikes.client.renderer.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.spikyspikes.client.model.geom.ShapeModelPart;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class SpikeRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    public static final Material BELL_RESOURCE_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("block/diamond_block"));
    private final ModelPart model = new ModelPart(ImmutableList.of(new ShapeModelPart.Pyramid(0, 0, 0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, 0.0F, 0.0F, 0.0F, false, 16.0F, 16.0F)), ImmutableMap.of());


    public SpikeRenderer(BlockEntityRendererProvider.Context p_173554_) {

    }

    @Override
    public void render(SpikeBlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
        VertexConsumer vertexconsumer = BELL_RESOURCE_LOCATION.buffer(p_112310_, RenderType::entitySolid);
        this.model.render(p_112309_, vertexconsumer, p_112311_, p_112312_);
    }
}
