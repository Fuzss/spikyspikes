package fuzs.spikyspikes.neoforge.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class NeoForgeSpikeBlock extends SpikeBlock {
    public static final MapCodec<SpikeBlock> CODEC = spikeCodec(NeoForgeSpikeBlock::new);

    public NeoForgeSpikeBlock(SpikeMaterial spikeMaterial, Properties properties) {
        super(spikeMaterial, properties);
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return PathType.DAMAGE_OTHER;
    }

    @Override
    public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
        return PathType.DANGER_OTHER;
    }
}
