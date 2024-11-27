package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.recipes.TransmuteShapelessRecipeBuilder;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.DECORATIONS, ModRegistry.WOODEN_SPIKE_ITEM.value(), 3)
                .define('S', Items.WOODEN_SWORD)
                .define('#', ItemTags.LOGS_THAT_BURN)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy(getHasName(Items.WOODEN_SWORD), this.has(Items.WOODEN_SWORD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.DECORATIONS, ModRegistry.STONE_SPIKE_ITEM.value(), 3)
                .define('S', Items.STONE_SWORD)
                .define('#', Items.SMOOTH_STONE)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy(getHasName(Items.STONE_SWORD), this.has(Items.STONE_SWORD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.DECORATIONS, ModRegistry.IRON_SPIKE_ITEM.value(), 3)
                .define('S', Items.IRON_SWORD)
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.IRON_SWORD), this.has(Items.IRON_SWORD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.DECORATIONS, ModRegistry.GOLDEN_SPIKE_ITEM.value(), 3)
                .define('S', Items.GOLDEN_SWORD)
                .define('#', Items.GOLD_INGOT)
                .define('@', Items.GOLD_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.GOLDEN_SWORD), this.has(Items.GOLDEN_SWORD))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.items(), RecipeCategory.DECORATIONS, ModRegistry.DIAMOND_SPIKE_ITEM.value(), 3)
                .define('S', Items.DIAMOND_SWORD)
                .define('#', Items.DIAMOND)
                .define('@', Items.DIAMOND_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy(getHasName(Items.DIAMOND_SWORD), this.has(Items.DIAMOND_SWORD))
                .save(recipeOutput);
        RecipeSerializer<?> recipeSerializer = TransmuteShapelessRecipeBuilder.getRecipeSerializer(SpikySpikes.MOD_ID);
        TransmuteShapelessRecipeBuilder.shapeless(recipeSerializer,
                        this.items(),
                        RecipeCategory.DECORATIONS,
                        ModRegistry.NETHERITE_SPIKE_ITEM.value())
                .requires(ModRegistry.DIAMOND_SPIKE_ITEM.value())
                .requires(Items.NETHERITE_INGOT)
                .input(ModRegistry.DIAMOND_SPIKE_ITEM.value())
                .unlockedBy(getHasName(Items.NETHERITE_INGOT), this.has(Items.NETHERITE_INGOT))
                .save(recipeOutput);
    }
}
