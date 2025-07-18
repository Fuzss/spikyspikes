package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.handler.SpikeLootingHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
abstract class LootItemRandomChanceWithEnchantedBonusConditionMixin {
    @Shadow
    @Final
    public Holder<Enchantment> enchantment;

    @ModifyVariable(method = "test", at = @At("STORE"), ordinal = 0)
    public int test(int enchantmentLevel, LootContext lootContext) {
        return SpikeLootingHandler.onComputeEnchantedLootBonus(this.enchantment, enchantmentLevel, lootContext);
    }
}
