//package fuzs.spikyspikes.mixin;
//
//import fuzs.spikyspikes.world.damagesource.LootingDamageSource;
//import net.minecraft.world.level.storage.loot.LootContext;
//import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
//import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
//
//@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
//abstract class LootItemRandomChanceWithEnchantedBonusConditionMixin {
//
//    @ModifyVariable(method = "test", at = @At("LOAD"), ordinal = 0)
//    public int test(int lootingLevel, LootContext context) {
//        if (context.hasParam(LootContextParams.DAMAGE_SOURCE) && context.getParam(LootContextParams.DAMAGE_SOURCE) instanceof LootingDamageSource damageSource) {
//            return damageSource.getLootingLevel().orElse(lootingLevel);
//        } else {
//            return lootingLevel;
//        }
//    }
//}
