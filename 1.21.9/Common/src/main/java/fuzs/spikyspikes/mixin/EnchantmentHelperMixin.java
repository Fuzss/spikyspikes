package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.handler.SpikeLootingHandler;
import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {

    @Inject(method = "getComponentType", at = @At("HEAD"), cancellable = true)
    private static void getComponentType(ItemStack itemStack, CallbackInfoReturnable<DataComponentType<ItemEnchantments>> callback) {
        if (itemStack.getItem() instanceof SpikeItem item && item.getSpikeMaterial().dropsPlayerLoot()) {
            callback.setReturnValue(DataComponents.STORED_ENCHANTMENTS);
        }
    }

    @ModifyVariable(method = "lambda$processEquipmentDropChance$25", at = @At("HEAD"), argsOnly = true)
    private static int processEquipmentDropChance$0(int enchantmentLevel, ServerLevel level, LivingEntity entity, DamageSource damageSource, MutableFloat mutableFloat, RandomSource randomSource, Holder<Enchantment> enchantment, int _enchantmentLevel, EnchantedItemInUse enchantedItemInUse) {
        return SpikeLootingHandler.onComputeEnchantedLootBonus(enchantment, enchantmentLevel, entity, damageSource);
    }

    @ModifyVariable(method = "lambda$processEquipmentDropChance$27", at = @At("HEAD"), argsOnly = true)
    private static int processEquipmentDropChance$1(int enchantmentLevel, ServerLevel level, LivingEntity entity, DamageSource damageSource, MutableFloat mutableFloat, RandomSource randomSource, Holder<Enchantment> enchantment, int _enchantmentLevel, EnchantedItemInUse enchantedItemInUse) {
        return SpikeLootingHandler.onComputeEnchantedLootBonus(enchantment, enchantmentLevel, entity, damageSource);
    }
}
