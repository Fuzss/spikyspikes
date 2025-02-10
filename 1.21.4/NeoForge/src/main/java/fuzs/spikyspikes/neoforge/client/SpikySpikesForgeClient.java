package fuzs.spikyspikes.neoforge.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.SpikySpikesClient;
import fuzs.spikyspikes.data.client.ModLanguageProvider;
import fuzs.spikyspikes.data.client.ModModelProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = SpikySpikes.MOD_ID, dist = Dist.CLIENT)
public class SpikySpikesForgeClient {

    public SpikySpikesForgeClient() {
        ClientModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikesClient::new);
        DataProviderHelper.registerDataProviders(SpikySpikes.MOD_ID, ModLanguageProvider::new, ModModelProvider::new);
    }
}
