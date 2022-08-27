package fuzs.spikyspikes.data;

import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator p_126511_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, modId, existingFileHelper);
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