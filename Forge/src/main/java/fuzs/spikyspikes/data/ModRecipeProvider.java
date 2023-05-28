package fuzs.spikyspikes.data;

import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

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
                .unlockedBy("has_wooden_sword", has(Items.WOODEN_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.STONE_SPIKE_ITEM.get(), 3)
                .define('S', Items.STONE_SWORD)
                .define('#', Items.SMOOTH_STONE)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy("has_stone_sword", has(Items.STONE_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.IRON_SPIKE_ITEM.get(), 3)
                .define('S', Items.IRON_SWORD)
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_iron_sword", has(Items.IRON_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.GOLDEN_SPIKE_ITEM.get(), 3)
                .define('S', Items.GOLDEN_SWORD)
                .define('#', Items.GOLD_INGOT)
                .define('@', Items.GOLD_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_golden_sword", has(Items.GOLDEN_SWORD))
                .save(exporter);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.DIAMOND_SPIKE_ITEM.get(), 3)
                .define('S', Items.DIAMOND_SWORD)
                .define('#', Items.DIAMOND)
                .define('@', Items.DIAMOND_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_diamond_sword", has(Items.DIAMOND_SWORD))
                .save(exporter);
        legacyNetheriteSmithing(SpikySpikes.MOD_ID, exporter, ModRegistry.DIAMOND_SPIKE_ITEM.get(), RecipeCategory.DECORATIONS, ModRegistry.NETHERITE_SPIKE_ITEM.get());
    }

    protected static void legacyNetheriteSmithing(String modId, Consumer<FinishedRecipe> exporter, Item base, RecipeCategory category, Item result) {
        LegacyUpgradeRecipeBuilder.smithing(Ingredient.of(base), Ingredient.of(Items.NETHERITE_INGOT), category, result)
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(exporter, new ResourceLocation(modId, getItemName(result) + "_smithing"));
        new SmithingTransformRecipeBuilder(ModRegistry.LEGACY_SMITHING_TRANSFORM_RECIPE_SERIALIZER.get(), Ingredient.of(), Ingredient.of(base), Ingredient.of(Items.NETHERITE_INGOT), category, result)
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(exporter, new ResourceLocation(modId, getItemName(result) + "_smithing_transform"));
    }
}
