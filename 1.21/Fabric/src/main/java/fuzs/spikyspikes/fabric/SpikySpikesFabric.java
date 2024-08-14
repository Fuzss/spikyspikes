package fuzs.spikyspikes.fabric;

import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class SpikySpikesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikes::new);
        registerBlockPathTypes();
    }

    private static void registerBlockPathTypes() {
        LandPathNodeTypesRegistry.register(ModRegistry.WOODEN_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
        LandPathNodeTypesRegistry.register(ModRegistry.STONE_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
        LandPathNodeTypesRegistry.register(ModRegistry.IRON_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
        LandPathNodeTypesRegistry.register(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
        LandPathNodeTypesRegistry.register(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
        LandPathNodeTypesRegistry.register(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), BlockPathTypes.DAMAGE_OTHER, BlockPathTypes.DANGER_OTHER);
    }
}
