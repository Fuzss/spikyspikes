package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.handler.SpikeEventHandler;
import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
abstract class EnchantmentMixin {
    @Shadow
    @Final
    public EnchantmentCategory category;

    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    public void canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        // this is only used for the /enchant command, anvil behavior is overridden in an event and enchanting tables are not affected by this method
        if (stack.getItem() instanceof SpikeItem spikeItem && spikeItem.acceptsEnchantments()) {
            callback.setReturnValue(SpikeEventHandler.canEnchantSpike(this.category));
        }
    }
}
