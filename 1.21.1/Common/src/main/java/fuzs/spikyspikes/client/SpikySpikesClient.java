package fuzs.spikyspikes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SpikySpikesClient implements ClientModConstructor {

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), SpikeRenderer::new);
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer(
                (ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource vertexConsumers, int packedLight, int packedOverlay) -> {
                    SpikeBlock block = (SpikeBlock) ((BlockItem) itemStack.getItem()).getBlock();
                    SpikeRenderer.renderSpike(Direction.UP, block.getSpikeMaterial(), poseStack, vertexConsumers, packedLight,
                            packedOverlay, true, itemStack.hasFoil()
                    );
                }, ModRegistry.WOODEN_SPIKE_ITEM.value(), ModRegistry.STONE_SPIKE_ITEM.value(),
                ModRegistry.IRON_SPIKE_ITEM.value(), ModRegistry.GOLDEN_SPIKE_ITEM.value(),
                ModRegistry.DIAMOND_SPIKE_ITEM.value(), ModRegistry.NETHERITE_SPIKE_ITEM.value()
        );
    }
}
