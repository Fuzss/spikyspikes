package fuzs.spikyspikes.data;

import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.builtInBlock(ModRegistry.WOODEN_SPIKE_BLOCK.get(), Blocks.OAK_PLANKS);
        this.builtInBlock(ModRegistry.STONE_SPIKE_BLOCK.get(), Blocks.SMOOTH_STONE);
        this.builtInBlock(ModRegistry.IRON_SPIKE_BLOCK.get(), Blocks.IRON_BLOCK);
        this.builtInBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.get(), Blocks.GOLD_BLOCK);
        this.builtInBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), Blocks.DIAMOND_BLOCK);
        this.builtInBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), Blocks.NETHERITE_BLOCK);
    }

    public void builtInBlock(Block block, Block texture) {
        this.simpleBlock(block, this.models().getBuilder(this.name(block))
                .texture("particle", this.blockTexture(texture)));
    }

    public String name(Block block) {
        return Registry.BLOCK.getKey(block).getPath();
    }
}
