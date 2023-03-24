package fuzs.spikyspikes;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class SpikySpikesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
    }
}
