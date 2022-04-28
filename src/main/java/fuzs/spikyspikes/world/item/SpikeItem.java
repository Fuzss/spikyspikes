package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.client.renderer.ModBlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class SpikeItem extends BlockItem {
    public SpikeItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(ModBlockEntityWithoutLevelRenderer.createItemRenderProperties(this));
    }
}
