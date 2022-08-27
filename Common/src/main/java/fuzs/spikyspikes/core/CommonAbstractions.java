package fuzs.spikyspikes.core;

import net.minecraft.world.item.ItemStack;

public interface CommonAbstractions {

    default boolean isStackBookEnchantable(ItemStack stack, ItemStack bookStack) {
        return true;
    }
}
