package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v1.recipes.CopyTagShapelessRecipeBuilder;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.WOODEN_SPIKE_ITEM.get(), 3)
                .define('S', Items.WOODEN_SWORD)
                .define('#', ItemTags.LOGS_THAT_BURN)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy(getHasName(Items.WOODEN_SWORD), has(Items.WOODEN_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.STONE_SPIKE_ITEM.get(), 3)
                .define('S', Items.STONE_SWORD)
                .define('#', Items.SMOOTH_STONE)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy(getHasName(Items.STONE_SWORD), has(Items.STONE_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.IRON_SPIKE_ITEM.get(), 3)
                .define('S', Items.IRON_SWORD)
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.IRON_SWORD), has(Items.IRON_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.GOLDEN_SPIKE_ITEM.get(), 3)
                .define('S', Items.GOLDEN_SWORD)
                .define('#', Items.GOLD_INGOT)
                .define('@', Items.GOLD_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.GOLDEN_SWORD), has(Items.GOLDEN_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.DIAMOND_SPIKE_ITEM.get(), 3)
                .define('S', Items.DIAMOND_SWORD)
                .define('#', Items.DIAMOND)
                .define('@', Items.DIAMOND_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.DIAMOND_SWORD), has(Items.DIAMOND_SWORD))
                .save(exporter);
        CopyTagShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModRegistry.NETHERITE_SPIKE_ITEM.get())
                .requires(ModRegistry.DIAMOND_SPIKE_ITEM.get())
                .requires(Items.NETHERITE_INGOT)
                .copyFrom(ModRegistry.DIAMOND_SPIKE_ITEM.get())
                .unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(exporter);
    }
}
