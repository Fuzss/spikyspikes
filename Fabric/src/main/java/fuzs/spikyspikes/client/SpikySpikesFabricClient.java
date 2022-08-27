package fuzs.spikyspikes.client;

import fuzs.puzzleslib.client.core.ClientCoreServices;
import fuzs.spikyspikes.SpikySpikes;
import net.fabricmc.api.ClientModInitializer;

public class SpikySpikesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCoreServices.FACTORIES.clientModConstructor(SpikySpikes.MOD_ID).accept(new SpikySpikesClient());
    }
}
