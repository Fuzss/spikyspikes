package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.api.world.damagesource.PlayerDamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * trick stolen from Darkhax's Bookshelf library (https://github.com/Darkhax-Minecraft/Bookshelf/blob/1.18.2/Common/src/main/java/net/darkhax/bookshelf/mixin/loot/MixinLootItemKilledByPlayerCondition.java)
 * avoids having to use a fake player when aiming for player only drops without an actual player
 */
@Mixin(LootItemKilledByPlayerCondition.class)
public abstract class LootItemKilledByPlayerConditionMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    public void test$head(LootContext context, CallbackInfoReturnable<Boolean> callback) {
        if (context != null && context.hasParam(LootContextParams.DAMAGE_SOURCE) && context.getParam(LootContextParams.DAMAGE_SOURCE) instanceof PlayerDamageSource source) {
            if (source.dropPlayerLoot(context)) {
                callback.setReturnValue(true);
            }
        }
    }
}
