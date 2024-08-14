package fuzs.spikyspikes.fabric.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.SpikySpikesClient;
import net.fabricmc.api.ClientModInitializer;

public class SpikySpikesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikesClient::new);
    }
}
