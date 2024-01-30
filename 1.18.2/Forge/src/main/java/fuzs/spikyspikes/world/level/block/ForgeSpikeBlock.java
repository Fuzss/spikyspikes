package fuzs.spikyspikes.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

public class ForgeSpikeBlock extends SpikeBlock {

    public ForgeSpikeBlock(SpikeMaterial spikeMaterial, Properties properties) {
        super(spikeMaterial, properties);
    }

    @Nullable
    @Override
    public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.DAMAGE_OTHER;
    }

//    @Override
//    public @Nullable BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
//        return BlockPathTypes.DAMAGE_OTHER;
//    }
//
//    @Override
//    public @Nullable BlockPathTypes getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, BlockPathTypes originalType) {
//        return BlockPathTypes.DANGER_OTHER;
//    }
}
