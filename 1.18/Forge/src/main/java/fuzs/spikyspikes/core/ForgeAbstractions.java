package fuzs.spikyspikes.core;

import net.minecraft.world.item.ItemStack;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isStackBookEnchantable(ItemStack stack, ItemStack bookStack) {
        return stack.isBookEnchantable(bookStack);
    }
}
