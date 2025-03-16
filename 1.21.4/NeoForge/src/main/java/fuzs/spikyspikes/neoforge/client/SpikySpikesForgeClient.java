package fuzs.spikyspikes.neoforge.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.SpikySpikesClient;
import fuzs.spikyspikes.data.client.ModLanguageProvider;
import fuzs.spikyspikes.data.client.ModModelProvider;
import fuzs.spikyspikes.util.DestroyEffectsHelper;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@Mod(value = SpikySpikes.MOD_ID, dist = Dist.CLIENT)
public class SpikySpikesForgeClient {

    public SpikySpikesForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikesClient::new);
        DataProviderHelper.registerDataProviders(SpikySpikes.MOD_ID, ModLanguageProvider::new, ModModelProvider::new);
//        registerLoadingHandlers(modContainer.getEventBus());
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        eventBus.addListener((final RegisterClientExtensionsEvent evt) -> {
            for (Block block : BuiltInRegistries.BLOCK) {
                if (block instanceof SpikeBlock) {
                    evt.registerBlock(new IClientBlockExtensions() {
                        @Override
                        public boolean addDestroyEffects(BlockState blockState, Level level, BlockPos blockPos, ParticleEngine particleEngine) {
                            return DestroyEffectsHelper.addDestroyEffects(blockState,
                                    level,
                                    blockPos,
                                    particleEngine) || IClientBlockExtensions.super.addDestroyEffects(blockState,
                                    level,
                                    blockPos,
                                    particleEngine);
                        }
                    }, block);
                }
            }
        });
    }
}
