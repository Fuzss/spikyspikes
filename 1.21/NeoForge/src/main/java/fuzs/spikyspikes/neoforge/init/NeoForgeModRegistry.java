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

public class NeoForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(SpikySpikes.MOD_ID);
    public static final Holder.Reference<Block> WOODEN_SPIKE_BLOCK = REGISTRY.registerBlock("wooden_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.WOOD, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Holder.Reference<Block> STONE_SPIKE_BLOCK = REGISTRY.registerBlock("stone_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.STONE, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final Holder.Reference<Block> IRON_SPIKE_BLOCK = REGISTRY.registerBlock("iron_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.IRON, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> GOLDEN_SPIKE_BLOCK = REGISTRY.registerBlock("golden_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.GOLD, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> DIAMOND_SPIKE_BLOCK = REGISTRY.registerBlock("diamond_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.DIAMOND, BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> NETHERITE_SPIKE_BLOCK = REGISTRY.registerBlock("netherite_spike", () -> new NeoForgeSpikeBlock(SpikeMaterial.NETHERITE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)));

    public static void touch() {

    }
}
