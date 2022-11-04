package fuzs.spikyspikes.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

/**
 * only necessary for setting custom {@link BlockPathTypes}, not possible on Fabric this easily, so we just skip it
 */
public class ForgeSpikeBlock extends SpikeBlock {

    public ForgeSpikeBlock(SpikeMaterial spikeMaterial, Properties p_49795_) {
        super(spikeMaterial, p_49795_);
    }

    @Override
    public @Nullable BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.DAMAGE_CACTUS;
    }
}
