package fuzs.spikyspikes.handler;

import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public class SpikeLootingHandler {

    public static void onComputeEnchantedLootBonus(LivingEntity entity, @Nullable DamageSource damageSource, Holder<Enchantment> enchantment, MutableInt enchantmentLevel) {
        if (damageSource instanceof SpikeDamageSource spikeDamageSource) {
            enchantmentLevel.accept(spikeDamageSource.getItemEnchantments().getLevel(enchantment));
        }
    }
}
