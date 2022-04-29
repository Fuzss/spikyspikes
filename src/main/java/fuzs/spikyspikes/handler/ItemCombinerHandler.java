package fuzs.spikyspikes.handler;

import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ItemCombinerHandler {

    @SubscribeEvent
    public void onAnvilUpdate(final AnvilUpdateEvent evt) {
        ItemStack itemstack = evt.getLeft();
        ItemStack itemstack2 = evt.getRight();
        if (itemstack.getItem() instanceof SpikeItem spikeItem && spikeItem.acceptsEnchantments() && itemstack2.getItem() instanceof EnchantedBookItem) {
            if (!EnchantedBookItem.getEnchantments(itemstack2).isEmpty()) {
                int i = 0;
                int j = 0;
                int k = 0;
                ItemStack itemstack1 = itemstack.copy();
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
                j += itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
                evt.setMaterialCost(0);

                Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                boolean flag2 = false;
                boolean enchantingDisallowed = false;

                for (Enchantment enchantment1 : map1.keySet()) {
                    if (enchantment1 != null) {
                        int i2 = map.getOrDefault(enchantment1, 0);
                        int j2 = map1.get(enchantment1);
                        j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                        boolean canEnchant = enchantment1.category == EnchantmentCategory.WEAPON;
                        if (evt.getPlayer().getAbilities().instabuild) {
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
                            if (j2 > enchantment1.getMaxLevel()) {
                                j2 = enchantment1.getMaxLevel();
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
                    return;
                }

                String itemName = evt.getName();
                if (StringUtils.isBlank(itemName)) {
                    if (itemstack.hasCustomHoverName()) {
                        k = 1;
                        i += k;
                        itemstack1.resetHoverName();
                    }
                } else if (!itemName.equals(itemstack.getHoverName().getString())) {
                    k = 1;
                    i += k;
                    itemstack1.setHoverName(new TextComponent(itemName));
                }
                if (!itemstack1.isBookEnchantable(itemstack2)) itemstack1 = ItemStack.EMPTY;

                evt.setCost(j + i);
                if (i <= 0) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (k == i && k > 0 && evt.getCost() >= 40) {
                    evt.setCost(39);
                }

                if (evt.getCost() >= 40 && !evt.getPlayer().getAbilities().instabuild) {
                    itemstack1 = ItemStack.EMPTY;
                }

                if (!itemstack1.isEmpty()) {
                    int k2 = itemstack1.getBaseRepairCost();
                    if (!itemstack2.isEmpty() && k2 < itemstack2.getBaseRepairCost()) {
                        k2 = itemstack2.getBaseRepairCost();
                    }

                    if (k != i || k == 0) {
                        k2 = AnvilMenu.calculateIncreasedRepairCost(k2);
                    }

                    itemstack1.setRepairCost(k2);
                    EnchantmentHelper.setEnchantments(map, itemstack1);
                }
                evt.setOutput(itemstack1);
                evt.getPlayer().containerMenu.broadcastChanges();
            }
        }
    }
}
