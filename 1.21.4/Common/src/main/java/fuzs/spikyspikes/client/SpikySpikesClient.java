package fuzs.spikyspikes.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelsContext;
import fuzs.puzzleslib.api.client.event.v1.model.ModelLoadingEvents;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.handler.SpikeBlockModelHandler;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.client.renderer.special.SpikeSpecialRenderer;
import fuzs.spikyspikes.init.ModRegistry;

public class SpikySpikesClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        // TODO remove this check once the custom block model is supported for NeoForge
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike()) {
            ModelLoadingEvents.LOAD_MODEL.register(SpikeBlockModelHandler::onLoadModel);
        }
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), SpikeRenderer::new);
    }

    @Override
    public void onRegisterItemModels(ItemModelsContext context) {
        // TODO remove once the custom block model is supported for NeoForge
        context.registerSpecialModelRenderer(SpikySpikes.id("spike"), SpikeSpecialRenderer.Unbaked.MAP_CODEC);
    }
}
