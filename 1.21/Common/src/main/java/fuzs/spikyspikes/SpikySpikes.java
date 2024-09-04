package fuzs.spikyspikes;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.core.v1.context.FuelBurnTimesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.living.ComputeLootingLevelCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.AnvilEvents;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import fuzs.spikyspikes.config.ServerConfig;
import fuzs.spikyspikes.handler.SpikeEventHandler;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpikySpikes implements ModConstructor {
    public static final String MOD_ID = "spikyspikes";
    public static final String MOD_NAME = "Spiky Spikes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
//        ComputeLootingLevelCallback.EVENT.register(SpikeEventHandler::onLootingLevel);
        AnvilEvents.UPDATE.register(SpikeEventHandler::onAnvilUpdate);
    }

    @Override
    public void onRegisterFuelBurnTimes(FuelBurnTimesContext context) {
        context.registerFuel(300, ModRegistry.WOODEN_SPIKE_BLOCK.value());
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(
                CreativeModeTabConfigurator.from(MOD_ID, () -> new ItemStack(ModRegistry.DIAMOND_SPIKE_ITEM.value()))
                        .displayItems((itemDisplayParameters, output) -> {
                            output.accept(ModRegistry.WOODEN_SPIKE_ITEM.value());
                            output.accept(ModRegistry.STONE_SPIKE_ITEM.value());
                            output.accept(ModRegistry.IRON_SPIKE_ITEM.value());
                            output.accept(ModRegistry.GOLDEN_SPIKE_ITEM.value());
                            output.accept(ModRegistry.DIAMOND_SPIKE_ITEM.value());
                            output.accept(ModRegistry.NETHERITE_SPIKE_ITEM.value());
                        }));
    }

    @Override
    public ContentRegistrationFlags[] getContentRegistrationFlags() {
        return new ContentRegistrationFlags[]{ContentRegistrationFlags.COPY_RECIPES};
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
