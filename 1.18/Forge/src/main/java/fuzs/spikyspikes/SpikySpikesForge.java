package fuzs.spikyspikes;

import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.spikyspikes.data.*;
import fuzs.spikyspikes.init.ForgeModRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikesForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new, ContentRegistrationFlags.COPY_TAG_RECIPES);
        ForgeModRegistry.touch();
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        evt.getGenerator().addProvider(new ModBlockLootProvider(evt, SpikySpikes.MOD_ID));
        evt.getGenerator().addProvider(new ModBlockTagsProvider(evt, SpikySpikes.MOD_ID));
//        dataGenerator.addProvider(new ModDamageTypeProvider(evt, SpikySpikes.MOD_ID));
        evt.getGenerator().addProvider(new ModLanguageProvider(evt, SpikySpikes.MOD_ID));
        evt.getGenerator().addProvider(new ModModelProvider(evt, SpikySpikes.MOD_ID));
        evt.getGenerator().addProvider(new ModRecipeProvider(evt, SpikySpikes.MOD_ID));
    }
}
