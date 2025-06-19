package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {

    @Inject(method = "getComponentType", at = @At("HEAD"), cancellable = true)
    private static void getComponentType(ItemStack itemStack, CallbackInfoReturnable<DataComponentType<ItemEnchantments>> callback) {
        if (itemStack.getItem() instanceof SpikeItem item && item.getSpikeMaterial().dropsPlayerLoot()) {
            callback.setReturnValue(DataComponents.STORED_ENCHANTMENTS);
        }
    }
}
