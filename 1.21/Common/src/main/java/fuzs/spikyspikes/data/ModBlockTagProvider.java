package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

public class ModBlockTagProvider extends AbstractTagProvider.Blocks {

    public ModBlockTagProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModRegistry.WOODEN_SPIKE_BLOCK.value());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.STONE_SPIKE_BLOCK.value(), ModRegistry.IRON_SPIKE_BLOCK.value(), ModRegistry.GOLDEN_SPIKE_BLOCK.value(), ModRegistry.DIAMOND_SPIKE_BLOCK.value(), ModRegistry.NETHERITE_SPIKE_BLOCK.value());
        this.tag(BlockTags.WITHER_IMMUNE).add(ModRegistry.NETHERITE_SPIKE_BLOCK.value());
    }
}