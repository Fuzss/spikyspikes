package fuzs.spikyspikes;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.spikyspikes.api.event.AnvilUpdateCallback;
import fuzs.spikyspikes.api.event.entity.living.LootingLevelCallback;
import fuzs.spikyspikes.handler.ItemCombinerHandler;
import fuzs.spikyspikes.handler.SpikeLootHandler;
import fuzs.spikyspikes.init.FabricModRegistry;
import net.fabricmc.api.ModInitializer;

public class SpikySpikesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // do this before common, so that blocks will already be registered as they are required later
        FabricModRegistry.touch();
        CoreServices.FACTORIES.modConstructor(SpikySpikes.MOD_ID).accept(new SpikySpikes());
        registerHandlers();
    }

    private static void registerHandlers() {
        SpikeLootHandler spikeLootHandler = new SpikeLootHandler();
        LootingLevelCallback.EVENT.register(spikeLootHandler::onLootingLevel);
        ItemCombinerHandler itemCombinerHandler = new ItemCombinerHandler();
        AnvilUpdateCallback.EVENT.register(itemCombinerHandler::onAnvilUpdate);
    }
}
