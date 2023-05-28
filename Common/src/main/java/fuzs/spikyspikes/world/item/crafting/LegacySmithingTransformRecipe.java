package fuzs.spikyspikes.world.item.crafting;

import com.google.gson.JsonObject;
import fuzs.spikyspikes.init.ModRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

public class LegacySmithingTransformRecipe extends SmithingTransformRecipe {
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    public LegacySmithingTransformRecipe(ResourceLocation id, Ingredient base, Ingredient addition, ItemStack result) {
        super(id, Ingredient.of(), base, addition, result);
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.LEGACY_SMITHING_TRANSFORM_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<LegacySmithingTransformRecipe> {

        @Override
        public LegacySmithingTransformRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            return new LegacySmithingTransformRecipe(resourceLocation, base, addition, result);
        }

        @Override
        public LegacySmithingTransformRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            Ingredient base = Ingredient.fromNetwork(friendlyByteBuf);
            Ingredient addition = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack result = friendlyByteBuf.readItem();
            return new LegacySmithingTransformRecipe(resourceLocation, base, addition, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, LegacySmithingTransformRecipe smithingTransformRecipe) {
            smithingTransformRecipe.base.toNetwork(friendlyByteBuf);
            smithingTransformRecipe.addition.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(smithingTransformRecipe.result);
        }
    }
}
