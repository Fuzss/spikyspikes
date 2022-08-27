package fuzs.spikyspikes.core;

import net.minecraft.world.item.ItemStack;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean isStackBookEnchantable(ItemStack stack, ItemStack bookStack) {
        return true;
    }
}
