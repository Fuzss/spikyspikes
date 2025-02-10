package fuzs.spikyspikes.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ModelTemplateHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.renderer.special.SpikeSpecialRenderer;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate SPIKE_INVENTORY = ModelTemplateHelper.createItemModelTemplate(SpikySpikes.id(
            "template_spike"), TextureSlot.PARTICLE);

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        this.createSpikeBlock(ModRegistry.WOODEN_SPIKE_BLOCK.value(), Blocks.OAK_PLANKS, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.STONE_SPIKE_BLOCK.value(), Blocks.SMOOTH_STONE, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.IRON_SPIKE_BLOCK.value(), Blocks.IRON_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), Blocks.GOLD_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), Blocks.DIAMOND_BLOCK, blockModelGenerators);
        this.createSpikeBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), Blocks.NETHERITE_BLOCK, blockModelGenerators);
    }

    public final void createSpikeBlock(Block spikeBlock, Block particleBlock, BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createParticleOnlyBlock(spikeBlock, particleBlock);
        ResourceLocation resourceLocation = SPIKE_INVENTORY.create(spikeBlock.asItem(),
                TextureMapping.particle(particleBlock),
                blockModelGenerators.modelOutput);
        ItemModel.Unbaked unbaked = ItemModelUtils.specialModel(resourceLocation,
                new SpikeSpecialRenderer.Unbaked(((SpikeBlock) spikeBlock).getSpikeMaterial()));
        blockModelGenerators.itemModelOutput.accept(spikeBlock.asItem(), unbaked);
    }
}
