package fuzs.spikyspikes.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.client.renderer.special.SpikeSpecialRenderer;
import fuzs.spikyspikes.init.ModRegistry;

public class SpikySpikesClient implements ClientModConstructor {

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), SpikeRenderer::new);
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        context.registerSpecialModelRenderer(SpikySpikes.id("spike"), SpikeSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
