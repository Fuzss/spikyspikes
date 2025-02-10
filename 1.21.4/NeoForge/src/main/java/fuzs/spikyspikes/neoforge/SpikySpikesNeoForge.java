package fuzs.spikyspikes.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.data.ModBlockLootProvider;
import fuzs.spikyspikes.data.tags.ModBlockTagProvider;
import fuzs.spikyspikes.data.tags.ModItemTagProvider;
import fuzs.spikyspikes.data.ModRecipeProvider;
import fuzs.spikyspikes.data.ModDatapackRegistriesProvider;
import fuzs.spikyspikes.neoforge.init.NeoForgeModRegistry;
import net.neoforged.fml.common.Mod;

@Mod(SpikySpikes.MOD_ID)
public class SpikySpikesNeoForge {

    public SpikySpikesNeoForge() {
        NeoForgeModRegistry.bootstrap();
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
        DataProviderHelper.registerDataProviders(SpikySpikes.MOD_ID,
                ModBlockLootProvider::new,
                ModBlockTagProvider::new,
                ModItemTagProvider::new,
                ModDatapackRegistriesProvider::new,
                ModRecipeProvider::new);
    }
}
