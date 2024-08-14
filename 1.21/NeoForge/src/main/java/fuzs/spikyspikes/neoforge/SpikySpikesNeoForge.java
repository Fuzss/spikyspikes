package fuzs.spikyspikes.neoforge;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.data.ModBlockLootProvider;
import fuzs.spikyspikes.data.ModBlockTagProvider;
import fuzs.spikyspikes.data.ModRecipeProvider;
import fuzs.spikyspikes.data.client.ModLanguageProvider;
import fuzs.spikyspikes.data.client.ModModelProvider;
import fuzs.spikyspikes.neoforge.data.ModDamageTypeProvider;
import fuzs.spikyspikes.neoforge.init.NeoForgeModRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikesNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        NeoForgeModRegistry.touch();
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
        DataProviderHelper.registerDataProviders(SpikySpikes.MOD_ID,
                ModBlockLootProvider::new,
                ModBlockTagProvider::new,
                ModDamageTypeProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new,
                ModRecipeProvider::new
        );
    }
}
