package fuzs.spikyspikes.handler;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.spikyspikes.world.damagesource.LootingDamageSource;
import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

/**
 * Copied from {@link AnvilMenu#createResult()} for our case, unfortunately this does not allow using
 * <code>/enchant</code>, which is handled separately.
 * <p>
 * Intentionally done this way so that enchantments cannot be applied at the enchanting table.
 */
public class SpikeEventHandler {

    public static EventResult onAnvilUpdate(ItemStack primaryItem, ItemStack secondaryItem, MutableValue<ItemStack> outputItem, @Nullable String itemName, MutableInt enchantmentCost, MutableInt materialCost, Player player) {

        // use this custom hook for changing anvil behavior in addition to a mixin, so we can allow applying an enchantment
        // to an entire item stack and not just individual items which would be ridiculously expensive both level and enchanted book wise
        if (primaryItem.getItem() instanceof SpikeItem item && item.acceptsEnchantments() &&
                secondaryItem.getItem() instanceof EnchantedBookItem) {

            if (!EnchantmentHelper.getEnchantmentsForCrafting(secondaryItem).isEmpty()) {

                int anvilRepairCost = 0;
                int itemRepairCost = 0;
                int renameCost = 0;
                ItemStack itemStack = primaryItem.copy();
                ItemEnchantments.Mutable primaryEnchantments = new ItemEnchantments.Mutable(
                        itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY));
                itemRepairCost += primaryItem.getOrDefault(DataComponents.REPAIR_COST, 0) +
                        secondaryItem.getOrDefault(DataComponents.REPAIR_COST, 0);
                materialCost.accept(0);

                ItemEnchantments secondaryEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(secondaryItem);
                boolean flag2 = false;
                boolean enchantingDisallowed = false;

                for (Holder<Enchantment> secondaryEnchantment : secondaryEnchantments.keySet()) {

                    int primaryLevel = primaryEnchantments.getLevel(secondaryEnchantment);
                    int secondaryLevel = secondaryEnchantments.getLevel(secondaryEnchantment);
                    int maxLevel = primaryLevel == secondaryLevel ? secondaryLevel + 1 :
                            Math.max(secondaryLevel, primaryLevel);
                    boolean canEnchant = canEnchantSpike(secondaryEnchantment.value());

                    if (player.getAbilities().instabuild) {
                        canEnchant = true;
                    }

                    for (Holder<Enchantment> primaryEnchantment : primaryEnchantments.keySet()) {
                        if (primaryEnchantment != secondaryEnchantment &&
                                !Enchantment.areCompatible(primaryEnchantment, secondaryEnchantment)) {
                            canEnchant = false;
                            ++anvilRepairCost;
                        }
                    }

                    if (!canEnchant) {

                        enchantingDisallowed = true;
                    } else {

                        flag2 = true;
                        // different from vanilla to not reset levels above max, useful for semi-Apotheosis mod support
                        // at least kind of, since combining levels for values above vanilla doesn't work (would require using Apotheosis' custom max level hook,
                        // but don't want to constantly drag the whole mod around in dev), but at least higher levels won't be downgraded
                        if (primaryLevel == secondaryLevel && maxLevel > secondaryEnchantment.value().getMaxLevel()) {
                            maxLevel = secondaryLevel;
                        }

                        primaryEnchantments.set(secondaryEnchantment, maxLevel);
                        int anvilCost = secondaryEnchantment.value().getAnvilCost();
                        anvilCost = Math.max(1, anvilCost / 2);
                        anvilRepairCost += anvilCost * maxLevel;
                    }
                }

                if (enchantingDisallowed && !flag2) {
                    return EventResult.PASS;
                }

                if (itemName != null && !StringUtil.isBlank(itemName)) {
                    if (!itemName.equals(primaryItem.getHoverName().getString())) {
                        renameCost = 1;
                        anvilRepairCost += renameCost;
                        itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(itemName));
                    }
                } else if (primaryItem.has(DataComponents.CUSTOM_NAME)) {
                    renameCost = 1;
                    anvilRepairCost += renameCost;
                    itemStack.remove(DataComponents.CUSTOM_NAME);
                }

                if (!CommonAbstractions.INSTANCE.isBookEnchantable(itemStack, secondaryItem)) {
                    itemStack = ItemStack.EMPTY;
                }

                enchantmentCost.accept(itemRepairCost + anvilRepairCost);
                if (anvilRepairCost <= 0) {
                    itemStack = ItemStack.EMPTY;
                }

                if (renameCost == anvilRepairCost && renameCost > 0 && enchantmentCost.getAsInt() >= 40) {
                    enchantmentCost.accept(39);
                }

                if (enchantmentCost.getAsInt() >= 40 && !player.getAbilities().instabuild) {
                    itemStack = ItemStack.EMPTY;
                }

                if (!itemStack.isEmpty()) {
                    int k2 = itemStack.getOrDefault(DataComponents.REPAIR_COST, 0);
                    if (!secondaryItem.isEmpty() && k2 < secondaryItem.getOrDefault(DataComponents.REPAIR_COST, 0)) {
                        k2 = secondaryItem.getOrDefault(DataComponents.REPAIR_COST, 0);
                    }

                    if (renameCost != anvilRepairCost || renameCost == 0) {
                        k2 = AnvilMenu.calculateIncreasedRepairCost(k2);
                    }

                    itemStack.set(DataComponents.REPAIR_COST, k2);
                    itemStack.set(DataComponents.STORED_ENCHANTMENTS, primaryEnchantments.toImmutable());
                }

                outputItem.accept(itemStack);
                player.containerMenu.broadcastChanges();
                return EventResult.ALLOW;
            }
        }

        return EventResult.PASS;
    }

    public static boolean canEnchantSpike(Enchantment enchantment) {
        // this is meant to be a check for just the WEAPON enchantment category, but my Universal Enchants mod breaks this as it replaces all enchantment categories,
        // so we attempt to filter out all weapon enchantments with the following line
        // (use elytra as it excludes mending, unbreaking and vanishing curse, and enchantments from other mods aren't likely compatible with swords)
        return enchantment.getSupportedItems().contains(Items.DIAMOND_SWORD.builtInRegistryHolder()) &&
                !enchantment.getSupportedItems().contains(Items.ELYTRA.builtInRegistryHolder());
    }

    public static void onLootingLevel(LivingEntity entity, @Nullable DamageSource damageSource, MutableInt lootingLevel) {
        if (damageSource instanceof LootingDamageSource lootingDamageSource) {
            lootingDamageSource.getLootingLevel().ifPresent(lootingLevel);
        }
    }
}
