package fuzs.spikyspikes;

import fuzs.spikyspikes.data.ModLanguageProvider;
import fuzs.spikyspikes.data.ModLootTableProvider;
import fuzs.spikyspikes.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SpikySpikes.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpikySpikes {
    public static final String MOD_ID = "spikyspikes";
    public static final String MOD_NAME = "Spiky Spikes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {

    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModLootTableProvider(generator, MOD_ID));
        generator.addProvider(new ModLanguageProvider(generator, MOD_ID));
    }
}
