package fuzs.spikyspikes;

import fuzs.puzzleslib.core.CoreServices;
import fuzs.spikyspikes.api.event.AnvilUpdateCallback;
import fuzs.spikyspikes.api.event.entity.living.LootingLevelCallback;
import fuzs.spikyspikes.handler.ItemCombinerHandler;
import fuzs.spikyspikes.handler.SpikeLootHandler;
import net.fabricmc.api.ModInitializer;

public class SpikySpikesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(SpikySpikes.MOD_ID).accept(new SpikySpikes());
        registerHandlers();
    }

    private static void registerHandlers() {
        LootingLevelCallback.EVENT.register(SpikeLootHandler::onLootingLevel);
        AnvilUpdateCallback.EVENT.register(ItemCombinerHandler::onAnvilUpdate);
    }
}
