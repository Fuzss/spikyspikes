package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

public class SpikeItem extends BlockItem {

    public SpikeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        if (this.getSpikeMaterial().dropsPlayerLoot()) {
            return !itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        } else {
            return super.isFoil(itemStack);
        }
    }

    public SpikeMaterial getSpikeMaterial() {
        return ((SpikeBlock) this.getBlock()).getSpikeMaterial();
    }
}
