package fuzs.spikyspikes.data;

import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    private final String modId;

    public ModRecipeProvider(DataGenerator p_125973_, String modId) {
        super(p_125973_);
        this.modId = modId;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(ModRegistry.WOODEN_SPIKE_ITEM.get(), 3)
                .define('S', Items.WOODEN_SWORD)
                .define('#', ItemTags.LOGS_THAT_BURN)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy("has_wooden_sword", has(Items.WOODEN_SWORD))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.STONE_SPIKE_ITEM.get(), 3)
                .define('S', Items.STONE_SWORD)
                .define('#', Items.SMOOTH_STONE)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("###")
                .unlockedBy("has_stone_sword", has(Items.STONE_SWORD))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.IRON_SPIKE_ITEM.get(), 3)
                .define('S', Items.IRON_SWORD)
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_iron_sword", has(Items.IRON_SWORD))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.GOLDEN_SPIKE_ITEM.get(), 3)
                .define('S', Items.GOLDEN_SWORD)
                .define('#', Items.GOLD_INGOT)
                .define('@', Items.GOLD_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_golden_sword", has(Items.GOLDEN_SWORD))
                .save(p_176532_);
        ShapedRecipeBuilder.shaped(ModRegistry.DIAMOND_SPIKE_ITEM.get(), 3)
                .define('S', Items.DIAMOND_SWORD)
                .define('#', Items.DIAMOND)
                .define('@', Items.DIAMOND_BLOCK)
                .pattern(" S ")
                .pattern("S#S")
                .pattern("#@#")
                .unlockedBy("has_diamond_sword", has(Items.DIAMOND_SWORD))
                .save(p_176532_);
        this.netheriteSmithing2(p_176532_, ModRegistry.DIAMOND_SPIKE_ITEM.get(), ModRegistry.NETHERITE_SPIKE_ITEM.get());
    }

    protected void netheriteSmithing2(Consumer<FinishedRecipe> p_125995_, Item p_125996_, Item p_125997_) {
        // custom mod id path, otherwise same as vanilla method
        UpgradeRecipeBuilder.smithing(Ingredient.of(p_125996_), Ingredient.of(Items.NETHERITE_INGOT), p_125997_).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(p_125995_, new ResourceLocation(this.modId, getItemName(p_125997_) + "_smithing"));
    }
}
