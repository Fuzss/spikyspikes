package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

/**
 * we don't really need this, there is no special functionality, but let's leave it as a marker class
 */
public class SpikeItem extends BlockItem {

    public SpikeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockState = this.getBlock().getStateForPlacement(context);
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        return blockState != null && level.isUnobstructed(blockState, blockPos, CollisionContext.empty()) ? blockState :
                null;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (this.acceptsEnchantments()) {
            return !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        } else {
            return super.isFoil(stack);
        }
    }

    public boolean acceptsEnchantments() {
        return ((SpikeBlock) this.getBlock()).getSpikeMaterial().dropsPlayerLoot();
    }
}
