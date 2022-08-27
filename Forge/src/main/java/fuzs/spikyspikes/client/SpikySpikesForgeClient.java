package fuzs.spikyspikes.client;

import fuzs.puzzleslib.client.core.ClientCoreServices;
import fuzs.spikyspikes.SpikySpikes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = SpikySpikes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpikySpikesForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientCoreServices.FACTORIES.clientModConstructor(SpikySpikes.MOD_ID).accept(new SpikySpikesClient());
    }
}
