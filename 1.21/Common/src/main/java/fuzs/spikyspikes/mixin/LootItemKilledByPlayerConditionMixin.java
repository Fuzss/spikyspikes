package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.world.damagesource.LootingDamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Trick stolen from Darkhax's <a
 * href="https://github.com/Darkhax-Minecraft/Bookshelf/blob/1.18.2/Common/src/main/java/net/darkhax/bookshelf/mixin/loot/MixinLootItemKilledByPlayerCondition.java">Bookshelf
 * library</a>.
 * <p>
 * Avoids having to use a fake player when aiming for player only drops without an actual player.
 */
@Mixin(LootItemKilledByPlayerCondition.class)
abstract class LootItemKilledByPlayerConditionMixin {

    @Inject(method = "test", at = @At("HEAD"), cancellable = true)
    public void test(LootContext context, CallbackInfoReturnable<Boolean> callback) {
        if (context.getParamOrNull(LootContextParams.DAMAGE_SOURCE) instanceof LootingDamageSource) {
            callback.setReturnValue(true);
        }
    }
}
