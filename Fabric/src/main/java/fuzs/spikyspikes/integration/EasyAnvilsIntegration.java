package fuzs.spikyspikes.integration;

import fuzs.easyanvils.api.event.AnvilUpdateCallback;
import fuzs.spikyspikes.handler.ItemCombinerHandler;

public class EasyAnvilsIntegration {

    public static void registerHandlers() {
        AnvilUpdateCallback.EVENT.register(ItemCombinerHandler::onAnvilUpdate);
    }
}
