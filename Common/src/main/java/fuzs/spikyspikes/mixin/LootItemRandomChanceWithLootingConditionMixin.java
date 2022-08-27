package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.api.world.damagesource.PlayerDamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LootItemRandomChanceWithLootingCondition.class)
public abstract class LootItemRandomChanceWithLootingConditionMixin {

    @ModifyVariable(method = "test", at = @At("LOAD"), ordinal = 0)
    public int test$modifyVariable$load$lootingLevel(int lootingLevel, LootContext context) {
        // forge has a hook here as well, but it only triggers if a killer entity is present, which is not the case, so we need this custom hook
        if (context != null && context.hasParam(LootContextParams.DAMAGE_SOURCE) && context.getParam(LootContextParams.DAMAGE_SOURCE) instanceof PlayerDamageSource source) {
            return source.lootingLevel();
        }
        return lootingLevel;
    }
}
