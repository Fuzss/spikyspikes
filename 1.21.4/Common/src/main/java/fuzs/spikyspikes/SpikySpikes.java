package fuzs.spikyspikes;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.FuelValuesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.living.ComputeEnchantedLootBonusCallback;
import fuzs.spikyspikes.config.ServerConfig;
import fuzs.spikyspikes.handler.SpikeLootingHandler;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpikySpikes implements ModConstructor {
    public static final String MOD_ID = "spikyspikes";
    public static final String MOD_NAME = "Spiky Spikes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ComputeEnchantedLootBonusCallback.EVENT.register(SpikeLootingHandler::onComputeEnchantedLootBonus);
    }

    @Override
    public void onRegisterFuelValues(FuelValuesContext context) {
        context.registerFuel(ModRegistry.WOODEN_SPIKE_BLOCK, context.fuelBaseValue() * 3 / 2);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
