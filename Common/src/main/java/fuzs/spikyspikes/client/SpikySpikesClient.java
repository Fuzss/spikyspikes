package fuzs.spikyspikes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SpikySpikesClient implements ClientModConstructor {

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), context1 -> new SpikeRenderer());
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        registerBuiltinModelSpikeRenderer(context, ModRegistry.WOODEN_SPIKE_ITEM.get());
        registerBuiltinModelSpikeRenderer(context, ModRegistry.STONE_SPIKE_ITEM.get());
        registerBuiltinModelSpikeRenderer(context, ModRegistry.IRON_SPIKE_ITEM.get());
        registerBuiltinModelSpikeRenderer(context, ModRegistry.GOLDEN_SPIKE_ITEM.get());
        registerBuiltinModelSpikeRenderer(context, ModRegistry.DIAMOND_SPIKE_ITEM.get());
        registerBuiltinModelSpikeRenderer(context, ModRegistry.NETHERITE_SPIKE_ITEM.get());
    }

    private static void registerBuiltinModelSpikeRenderer(BuiltinModelItemRendererContext context, Item item) {
        context.register(item, (ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) -> {
            SpikeBlock block = ((SpikeItem) stack.getItem()).getBlock();
            SpikeRenderer.renderSpike(Direction.UP, block.spikeMaterial, matrices, vertexConsumers, light, overlay, true, stack.hasFoil());
        });
    }
}
