package fuzs.spikyspikes;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.spikyspikes.init.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class SpikySpikesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
        registerBlockPathTypes();
    }

    private static void registerBlockPathTypes() {
        registerSpikeBlockPathTypes(ModRegistry.WOODEN_SPIKE_BLOCK.get());
        registerSpikeBlockPathTypes(ModRegistry.STONE_SPIKE_BLOCK.get());
        registerSpikeBlockPathTypes(ModRegistry.IRON_SPIKE_BLOCK.get());
        registerSpikeBlockPathTypes(ModRegistry.GOLDEN_SPIKE_BLOCK.get());
        registerSpikeBlockPathTypes(ModRegistry.DIAMOND_SPIKE_BLOCK.get());
        registerSpikeBlockPathTypes(ModRegistry.NETHERITE_SPIKE_BLOCK.get());
    }

    private static void registerSpikeBlockPathTypes(Block block) {
        LandPathNodeTypesRegistry.register(block, BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
    }
}
