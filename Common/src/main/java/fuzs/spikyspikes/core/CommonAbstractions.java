package fuzs.spikyspikes.core;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.world.item.ItemStack;

public interface CommonAbstractions {

    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    default boolean isStackBookEnchantable(ItemStack stack, ItemStack bookStack) {
        return true;
    }
}
