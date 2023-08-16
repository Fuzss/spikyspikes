package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(GatherDataEvent evt, String modId) {
        super(evt, modId);
    }

    @Override
    protected void registerStatesAndModels() {
        this.builtInBlock(ModRegistry.WOODEN_SPIKE_BLOCK.get(), Blocks.OAK_PLANKS);
        this.builtInBlock(ModRegistry.STONE_SPIKE_BLOCK.get(), Blocks.SMOOTH_STONE);
        this.builtInBlock(ModRegistry.IRON_SPIKE_BLOCK.get(), Blocks.IRON_BLOCK);
        this.builtInBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.get(), Blocks.GOLD_BLOCK);
        this.builtInBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.get(), Blocks.DIAMOND_BLOCK);
        this.builtInBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.get(), Blocks.NETHERITE_BLOCK);
        this.itemModels().getBuilder("template_spike")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .transforms()
                .transform(ItemTransforms.TransformType.GUI)
                .rotation(30, 45, 0)
                .translation(0, 0, 0)
                .scale(0.625F, 0.625F, 0.625F)
                .end()
                .transform(ItemTransforms.TransformType.GROUND)
                .rotation(0, 0, 0)
                .translation(0, 3, 0)
                .scale(0.25F, 0.25F, 0.25F)
                .end()
                .transform(ItemTransforms.TransformType.HEAD)
                .rotation(0, 180, 0)
                .translation(0, 0, 0)
                .scale(1, 1, 1)
                .end()
                .transform(ItemTransforms.TransformType.FIXED)
                .rotation(0, 180, 0)
                .translation(0, 0, 0)
                .scale(0.5F, 0.5F, 0.5F)
                .end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 315, 0)
                .translation(0, 2.5F, 0)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 315, 0)
                .translation(0, 0, 0)
                .scale(0.4F, 0.4F, 0.4F)
                .end()
                .end();
        this.builtInItem(ModRegistry.WOODEN_SPIKE_ITEM.get(), Blocks.OAK_PLANKS, this.modLoc("template_spike"));
        this.builtInItem(ModRegistry.STONE_SPIKE_ITEM.get(), Blocks.SMOOTH_STONE, this.modLoc("template_spike"));
        this.builtInItem(ModRegistry.IRON_SPIKE_ITEM.get(), Blocks.IRON_BLOCK, this.modLoc("template_spike"));
        this.builtInItem(ModRegistry.GOLDEN_SPIKE_ITEM.get(), Blocks.GOLD_BLOCK, this.modLoc("template_spike"));
        this.builtInItem(ModRegistry.DIAMOND_SPIKE_ITEM.get(), Blocks.DIAMOND_BLOCK, this.modLoc("template_spike"));
        this.builtInItem(ModRegistry.NETHERITE_SPIKE_ITEM.get(), Blocks.NETHERITE_BLOCK, this.modLoc("template_spike"));
    }
}
