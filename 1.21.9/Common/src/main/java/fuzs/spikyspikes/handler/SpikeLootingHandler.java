package fuzs.spikyspikes.handler;

import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;

public class SpikeLootingHandler {

    public static int onComputeEnchantedLootBonus(Holder<Enchantment> enchantment, int enchantmentLevel, LootContext lootContext) {
        Entity entity = lootContext.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof LivingEntity livingEntity)) return enchantmentLevel;
        DamageSource damageSource = lootContext.getOptionalParameter(LootContextParams.DAMAGE_SOURCE);
        return onComputeEnchantedLootBonus(enchantment, enchantmentLevel, livingEntity, damageSource);
    }

    public static int onComputeEnchantedLootBonus(Holder<Enchantment> enchantment, int enchantmentLevel, LivingEntity livingEntity, @Nullable DamageSource damageSource) {
        MutableInt mutableInt = MutableInt.fromValue(enchantmentLevel);
        onComputeEnchantedLootBonus(livingEntity, damageSource, enchantment, mutableInt);
        return mutableInt.getAsInt();
    }

    public static void onComputeEnchantedLootBonus(LivingEntity entity, @Nullable DamageSource damageSource, Holder<Enchantment> enchantment, MutableInt enchantmentLevel) {
        if (damageSource instanceof SpikeDamageSource spikeDamageSource) {
            enchantmentLevel.accept(spikeDamageSource.getItemEnchantments().getLevel(enchantment));
        }
    }
}
