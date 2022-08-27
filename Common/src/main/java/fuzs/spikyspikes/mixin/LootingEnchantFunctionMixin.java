package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.api.world.damagesource.PlayerDamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootingEnchantFunction.class)
public abstract class LootingEnchantFunctionMixin extends LootItemConditionalFunction {
    @Shadow
    @Final
    NumberProvider value;
    @Shadow
    @Final
    int limit;

    protected LootingEnchantFunctionMixin(LootItemCondition[] p_80678_) {
        super(p_80678_);
    }

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    public void run$inject$head(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> callback) {
        // forge has a hook here as well, but it only triggers if a killer entity is present, which is not the case, so we need this custom hook
        if (context != null && context.hasParam(LootContextParams.DAMAGE_SOURCE) && context.getParam(LootContextParams.DAMAGE_SOURCE) instanceof PlayerDamageSource source) {
            int i = source.lootingLevel();
            if (i == 0) {
                callback.setReturnValue(stack);
                return;
            }

            float f = (float)i * this.value.getFloat(context);
            stack.grow(Math.round(f));
            if (this.hasLimit() && stack.getCount() > this.limit) {
                stack.setCount(this.limit);
            }
            callback.setReturnValue(stack);
        }
    }

    @Shadow
    abstract boolean hasLimit();
}
