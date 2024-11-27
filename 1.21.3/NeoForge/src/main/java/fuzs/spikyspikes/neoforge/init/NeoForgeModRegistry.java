package fuzs.spikyspikes.neoforge.init;

import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.neoforge.world.level.block.NeoForgeSpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class NeoForgeModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(SpikySpikes.MOD_ID);
    public static final Holder.Reference<Block> WOODEN_SPIKE_BLOCK = REGISTRIES.registerBlock("wooden_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.WOOD, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> STONE_SPIKE_BLOCK = REGISTRIES.registerBlock("stone_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.STONE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> IRON_SPIKE_BLOCK = REGISTRIES.registerBlock("iron_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.IRON, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> GOLDEN_SPIKE_BLOCK = REGISTRIES.registerBlock("golden_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.GOLD, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> DIAMOND_SPIKE_BLOCK = REGISTRIES.registerBlock("diamond_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.DIAMOND, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIAMOND)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .pushReaction(PushReaction.DESTROY));
    public static final Holder.Reference<Block> NETHERITE_SPIKE_BLOCK = REGISTRIES.registerBlock("netherite_spike",
            (BlockBehaviour.Properties properties) -> new NeoForgeSpikeBlock(SpikeMaterial.NETHERITE, properties),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
                    .strength(50.0F, 1200.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .pushReaction(PushReaction.DESTROY));

    public static void bootstrap() {
        // NO-OP
    }
}
