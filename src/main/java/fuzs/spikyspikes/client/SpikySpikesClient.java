package fuzs.spikyspikes.client;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SpikySpikes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpikySpikesClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        BlockEntityRenderers.register(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), context -> new SpikeRenderer());
    }
}
