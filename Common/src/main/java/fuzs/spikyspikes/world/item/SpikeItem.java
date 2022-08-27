package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * we don't really need this, there is no special functionality, but let's leave it as a marker class
 */
public class SpikeItem extends BlockItem {

    public SpikeItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public SpikeBlock getBlock() {
        return (SpikeBlock) super.getBlock();
    }

    public boolean acceptsEnchantments() {
        return this.getBlock().spikeMaterial.acceptsEnchantments();
    }
}
