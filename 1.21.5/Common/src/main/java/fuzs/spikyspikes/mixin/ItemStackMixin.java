package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.world.item.SpikeItem;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
abstract class ItemStackMixin implements DataComponentHolder {

    @Inject(method = "isEnchantable", at = @At("TAIL"), cancellable = true)
    public void isEnchantable(CallbackInfoReturnable<Boolean> callback) {
        if (this.getItem() instanceof SpikeItem spikeItem && spikeItem.acceptsEnchantments()) {
            ItemEnchantments itemEnchantments = this.get(DataComponents.STORED_ENCHANTMENTS);
            callback.setReturnValue(itemEnchantments != null && itemEnchantments.isEmpty());
        }
    }

    @Shadow
    public abstract Item getItem();
}
