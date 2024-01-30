package fuzs.spikyspikes.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.spikyspikes.core.CommonAbstractions;
import fuzs.spikyspikes.world.damagesource.LootingDamageSource;
import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * copied from {@link AnvilMenu#createResult()} for our case, unfortunately this does not allow using /enchant
 *
 * <p>intentionally done this way so that enchantments cannot be applied at the enchanting table,
 * otherwise alternative would be using <code>net.minecraftforge.common.extensions.IForgeItem#canApplyAtEnchantingTable</code>
 * or a mixin for {@link EnchantmentCategory#WEAPON}
 */
public class SpikeEventHandler {

    public static EventResult onAnvilUpdate(ItemStack left, ItemStack right, MutableValue<ItemStack> output, String name, MutableInt cost, MutableInt materialCost, Player player) {
        if (left.getItem() instanceof SpikeItem spikeItem && spikeItem.acceptsEnchantments() && right.getItem() instanceof EnchantedBookItem) {
            if (!EnchantedBookItem.getEnchantments(right).isEmpty()) {
                int i = 0;
                int j = 0;
                int k = 0;
                ItemStack itemstack1 = left.copy();
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
                j += left.getBaseRepairCost() + (right.isEmpty() ? 0 : right.getBaseRepairCost());
                materialCost.accept(0);

                Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(right);
                boolean flag2 = false;
                boolean enchantingDisallowed = false;

                for (Enchantment enchantment1 : map1.keySet()) {
                    if (enchantment1 != null) {
                        int i2 = map.getOrDefault(enchantment1, 0);
                        int j22 = map1.get(enchantment1);
                        int j2 = i2 == j22 ? j22 + 1 : Math.max(j22, i2);
                        // this is meant to be a check for just the WEAPON enchantment category, but my Universal Enchants mod breaks this as it replaces all enchantment categories
                        // so we attempt to filter out all weapon enchantments with the following line
                        // (use elytra as it excludes mending, unbreaking and vanishing curse, and enchantments from other mods aren't likely compatible with swords)
                        boolean canEnchant = enchantment1.category.canEnchant(Items.DIAMOND_SWORD) && !enchantment1.category.canEnchant(Items.ELYTRA);
                        if (player.getAbilities().instabuild) {
                            canEnchant = true;
                        }

                        for(Enchantment enchantment : map.keySet()) {
                            if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                                canEnchant = false;
                                ++i;
                            }
                        }

                        if (!canEnchant) {
                            enchantingDisallowed = true;
                        } else {
                            flag2 = true;
                            // different from vanilla to not reset levels above max, useful for semi-Apotheosis mod support
                            // at least kind of, since combining levels for values above vanilla doesn't work (would require using Apotheosis' custom max level hook,
                            // but don't want to constantly drag the whole mod around in dev), but at least higher levels won't be downgraded
                            if (i2 == j22 && j2 > enchantment1.getMaxLevel()) {
                                j2 = j22;
                            }

                            map.put(enchantment1, j2);
                            int k3 = switch (enchantment1.getRarity()) {
                                case COMMON -> 1;
                                case UNCOMMON -> 2;
                                case RARE -> 4;
                                case VERY_RARE -> 8;
                            };

                            k3 = Math.max(1, k3 / 2);

                            i += k3 * j2;
                        }
                    }
                }

                if (enchantingDisallowed && !flag2) {
                    return EventResult.PASS;
                }

                if (StringUtils.isBlank(name)) {
                    if (left.hasCustomHoverName()) {
                        k = 1;
                        i += k;
                        itemstack1.resetHoverName();
                    }
                } else if (!name.equals(left.getHoverName().getString())) {
                    k = 1;
                    i += k;
                    itemstack1.setHoverName(Component.literal(name));
                }

                if (!CommonAbstractions.INSTANCE.isStackBookEnchantable(itemstack1, right)) {
                    itemstack1 = ItemStack.EMPTY;
                }

                cost.accept(j + i);
                if (i <= 0) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (k == i && k > 0 && cost.getAsInt() >= 40) {
                    cost.accept(39);
                }

                if (cost.getAsInt() >= 40 && !player.getAbilities().instabuild) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (!itemstack1.isEmpty()) {
                    int k2 = itemstack1.getBaseRepairCost();
                    if (!right.isEmpty() && k2 < right.getBaseRepairCost()) {
                        k2 = right.getBaseRepairCost();
                    }

                    if (k != i || k == 0) {
                        k2 = AnvilMenu.calculateIncreasedRepairCost(k2);
                    }

                    itemstack1.setRepairCost(k2);
                    EnchantmentHelper.setEnchantments(map, itemstack1);
                }
                output.accept(itemstack1);
                player.containerMenu.broadcastChanges();
                return EventResult.ALLOW;
            }
        }
        return EventResult.PASS;
    }

    public static void onLootingLevel(LivingEntity entity, @Nullable DamageSource damageSource, fuzs.puzzleslib.api.event.v1.data.MutableInt lootingLevel) {
        if (damageSource instanceof LootingDamageSource source) source.getLootingLevel().ifPresent(lootingLevel);
    }
}
