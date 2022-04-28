package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.client.renderer.ModBlockEntityWithoutLevelRenderer;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class SpikeItem extends BlockItem {
    public SpikeItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public SpikeBlock getBlock() {
        return (SpikeBlock) super.getBlock();
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ModBlockEntityWithoutLevelRenderer.createItemRenderProperties());
    }

    public boolean acceptsEnchantments() {
        return this.getBlock().spikeMaterial.acceptsEnchantments();
    }
}
