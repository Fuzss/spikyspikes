package fuzs.spikyspikes.world.item;

import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
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
        BlockState state = this.getBlock().getStateForPlacement(context);
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return state != null && level.isUnobstructed(state, pos, CollisionContext.empty()) ? state : null;
    }

    public boolean acceptsEnchantments() {
        return ((SpikeBlock) this.getBlock()).getSpikeMaterial().acceptsEnchantments();
    }
}
