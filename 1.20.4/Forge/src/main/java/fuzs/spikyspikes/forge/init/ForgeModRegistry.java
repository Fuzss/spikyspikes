package fuzs.spikyspikes.forge.init;

import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.forge.world.level.block.ForgeSpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(SpikySpikes.MOD_ID);
    public static final Holder.Reference<Block> WOODEN_SPIKE_BLOCK = REGISTRY.registerBlock("wooden_spike", () -> new ForgeSpikeBlock(SpikeMaterial.WOOD, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final Holder.Reference<Block> STONE_SPIKE_BLOCK = REGISTRY.registerBlock("stone_spike", () -> new ForgeSpikeBlock(SpikeMaterial.STONE, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final Holder.Reference<Block> IRON_SPIKE_BLOCK = REGISTRY.registerBlock("iron_spike", () -> new ForgeSpikeBlock(SpikeMaterial.IRON, BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> GOLDEN_SPIKE_BLOCK = REGISTRY.registerBlock("golden_spike", () -> new ForgeSpikeBlock(SpikeMaterial.GOLD, BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> DIAMOND_SPIKE_BLOCK = REGISTRY.registerBlock("diamond_spike", () -> new ForgeSpikeBlock(SpikeMaterial.DIAMOND, BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final Holder.Reference<Block> NETHERITE_SPIKE_BLOCK = REGISTRY.registerBlock("netherite_spike", () -> new ForgeSpikeBlock(SpikeMaterial.NETHERITE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK)));

    public static void touch() {

    }
}
