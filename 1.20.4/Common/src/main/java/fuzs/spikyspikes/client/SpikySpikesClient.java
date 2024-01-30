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
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), context1 -> new SpikeRenderer());
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer((ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) -> {
            SpikeBlock block = (SpikeBlock) ((BlockItem) stack.getItem()).getBlock();
            SpikeRenderer.renderSpike(Direction.UP, block.spikeMaterial, matrices, vertexConsumers, light, overlay, true, stack.hasFoil());
        }, ModRegistry.WOODEN_SPIKE_ITEM.get(), ModRegistry.STONE_SPIKE_ITEM.get(), ModRegistry.IRON_SPIKE_ITEM.get(), ModRegistry.GOLDEN_SPIKE_ITEM.get(), ModRegistry.DIAMOND_SPIKE_ITEM.get(), ModRegistry.NETHERITE_SPIKE_ITEM.get());
    }
}
