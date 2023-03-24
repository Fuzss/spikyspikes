package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemModelProvider extends AbstractModelProvider {

    public ModItemModelProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
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
        this.builtInItem(ModRegistry.WOODEN_SPIKE_ITEM.get(), Blocks.OAK_PLANKS);
        this.builtInItem(ModRegistry.STONE_SPIKE_ITEM.get(), Blocks.SMOOTH_STONE);
        this.builtInItem(ModRegistry.IRON_SPIKE_ITEM.get(), Blocks.IRON_BLOCK);
        this.builtInItem(ModRegistry.GOLDEN_SPIKE_ITEM.get(), Blocks.GOLD_BLOCK);
        this.builtInItem(ModRegistry.DIAMOND_SPIKE_ITEM.get(), Blocks.DIAMOND_BLOCK);
        this.builtInItem(ModRegistry.NETHERITE_SPIKE_ITEM.get(), Blocks.NETHERITE_BLOCK);
    }

    private void builtInItem(Item item, Block texture) {
        this.itemModels().getBuilder(this.itemName(item))
                .parent(this.itemModels().getExistingFile(this.modLoc("template_spike")))
                .texture("particle", this.blockTexture(texture));
    }

    public String itemName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
    }
}
