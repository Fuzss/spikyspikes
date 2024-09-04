package fuzs.spikyspikes.world.level.block.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.Function;

public final class BlockEnchantmentHelper {
    private static final EnchantedItemInUse EMPTY_ENCHANTED_ITEM = new EnchantedItemInUse(ItemStack.EMPTY, null, null, Function.identity()::apply);

    private BlockEnchantmentHelper() {
        // NO-OP
    }

    public static double getAttributeValue(Holder<Attribute> holder, ItemEnchantments itemEnchantments) {
        AttributeInstance attributeInstance = new AttributeInstance(holder, Function.identity()::apply);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            entry.getKey()
                    .value()
                    .getEffects(EnchantmentEffectComponents.ATTRIBUTES)
                    .forEach((EnchantmentAttributeEffect enchantmentAttributeEffect) -> {
                        attributeInstance.addTransientModifier(
                                enchantmentAttributeEffect.getModifier(entry.getIntValue(), EquipmentSlot.MAINHAND));
                    });
        }
        return attributeInstance.getValue();
    }

    public static float modifyDamage(ServerLevel level, Entity entity, DamageSource damageSource, float damage, ItemEnchantments itemEnchantments) {
        MutableFloat mutableFloat = new MutableFloat(damage);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            entry.getKey().value().modifyDamage(level, entry.getIntValue(), ItemStack.EMPTY, entity, damageSource, mutableFloat);
        }
        return mutableFloat.floatValue();
    }

    public static void doPostAttackEffects(ServerLevel level, Entity entity, DamageSource damageSource, ItemEnchantments itemEnchantments) {
        if (entity instanceof LivingEntity livingEntity) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
                entry.getKey().value().doPostAttack(level, entry.getIntValue(), EMPTY_ENCHANTED_ITEM, EnchantmentTarget.ATTACKER, livingEntity, damageSource);
            }
        }
    }

    public static float modifyKnockback(ServerLevel level, Entity entity, DamageSource damageSource, float knockback, ItemEnchantments itemEnchantments) {
        MutableFloat mutableFloat = new MutableFloat(knockback);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            entry.getKey().value().modifyKnockback(level, entry.getIntValue(), ItemStack.EMPTY, entity, damageSource, mutableFloat);
        }
        return mutableFloat.floatValue();
    }
}
