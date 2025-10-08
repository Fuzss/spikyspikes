package fuzs.spikyspikes.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.client.data.v2.models.ModelTemplateHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate SPIKE_MODEL_TEMPLATE = ModelTemplateHelper.createBlockModelTemplate(SpikySpikes.id(
                    "spike"),
            TextureSlot.PARTICLE,
            TextureSlot.NORTH,
            TextureSlot.SOUTH,
            TextureSlot.EAST,
            TextureSlot.WEST,
            TextureSlot.DOWN);

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        this.createSpikeBlock(ModRegistry.WOODEN_SPIKE_BLOCK.value(),
                ModelLocationHelper.getBlockTexture(Blocks.STRIPPED_OAK_LOG),
                ModelLocationHelper.getBlockTexture(Blocks.STRIPPED_OAK_LOG, "_top"),
                blockModelGenerators);
        this.createSpikeBlock(ModRegistry.STONE_SPIKE_BLOCK.value(), Blocks.SMOOTH_STONE, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.IRON_SPIKE_BLOCK.value(), Blocks.IRON_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), Blocks.GOLD_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), Blocks.DIAMOND_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), Blocks.NETHERITE_BLOCK, blockModelGenerators);
    }

    public final void createSpikeBlock(Block spikeBlock, Block textureBlock, BlockModelGenerators blockModelGenerators) {
        ResourceLocation resourceLocation = ModelLocationHelper.getBlockTexture(textureBlock);
        this.createSpikeBlock(spikeBlock, resourceLocation, resourceLocation, blockModelGenerators);
    }

    public final void createSpikeBlock(Block spikeBlock, ResourceLocation textureLocation, ResourceLocation bottomTextureLocation, BlockModelGenerators blockModelGenerators) {
        TextureMapping textureMapping = new TextureMapping().put(TextureSlot.PARTICLE, textureLocation)
                .put(TextureSlot.DOWN, bottomTextureLocation)
                .put(TextureSlot.NORTH, textureLocation)
                .put(TextureSlot.SOUTH, textureLocation)
                .put(TextureSlot.EAST, textureLocation)
                .put(TextureSlot.WEST, textureLocation);
        ResourceLocation resourceLocation = SPIKE_MODEL_TEMPLATE.create(spikeBlock,
                textureMapping,
                blockModelGenerators.modelOutput);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(spikeBlock,
                        BlockModelGenerators.plainVariant(resourceLocation))
                .with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING));
    }
}
