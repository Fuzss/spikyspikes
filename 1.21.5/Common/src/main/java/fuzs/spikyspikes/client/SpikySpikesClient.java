package fuzs.spikyspikes.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.client.renderer.util.SpikeTooltipHelper;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;

public class SpikySpikesClient implements ClientModConstructor {

    @Override
    public void onClientSetup() {
        ItemTooltipRegistry.registerItemTooltip(SpikeBlock.class, SpikeTooltipHelper::appendHoverText);
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.value(), SpikeRenderer::new);
    }
}
