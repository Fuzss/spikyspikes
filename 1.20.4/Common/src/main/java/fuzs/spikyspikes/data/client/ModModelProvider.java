package fuzs.spikyspikes.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate SPIKE_INVENTORY = createItemModelTemplate(SpikySpikes.id("template_spike"));

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators builder) {
        createSpikeBlock(ModRegistry.WOODEN_SPIKE_BLOCK.value(), Blocks.OAK_PLANKS, builder);
        createSpikeBlock(ModRegistry.STONE_SPIKE_BLOCK.value(), Blocks.SMOOTH_STONE, builder);
        createSpikeBlock(ModRegistry.IRON_SPIKE_BLOCK.value(), Blocks.IRON_BLOCK, builder);
        createSpikeBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), Blocks.GOLD_BLOCK, builder);
        createSpikeBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), Blocks.DIAMOND_BLOCK, builder);
        createSpikeBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), Blocks.NETHERITE_BLOCK, builder);
    }

    private static void createSpikeBlock(Block spikeBlock, Block particleBlock, BlockModelGenerators builder) {
        builder.blockEntityModels(spikeBlock, particleBlock)
                .createWithCustomBlockItemModel(SPIKE_INVENTORY, spikeBlock);
    }

    public static ModelTemplate createModelTemplate(ResourceLocation blockModelLocation, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(decorateBlockModelLocation(blockModelLocation)), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createItemModelTemplate(ResourceLocation itemModelLocation, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(decorateItemModelLocation(itemModelLocation)), Optional.empty(), requiredSlots);
    }

    public static ModelTemplate createModelTemplate(ResourceLocation blockModelLocation, String suffix, TextureSlot... requiredSlots) {
        return new ModelTemplate(Optional.of(decorateBlockModelLocation(blockModelLocation)), Optional.of(suffix), requiredSlots);
    }
}
