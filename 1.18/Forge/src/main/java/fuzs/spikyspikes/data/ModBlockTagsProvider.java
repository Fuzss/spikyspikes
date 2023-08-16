package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractTagProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModBlockTagsProvider extends AbstractTagProvider.Blocks {

    public ModBlockTagsProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModRegistry.WOODEN_SPIKE_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.STONE_SPIKE_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.IRON_SPIKE_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.GOLDEN_SPIKE_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.DIAMOND_SPIKE_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.NETHERITE_SPIKE_BLOCK.get());
        this.tag(BlockTags.WITHER_IMMUNE).add(ModRegistry.NETHERITE_SPIKE_BLOCK.get());
    }
}