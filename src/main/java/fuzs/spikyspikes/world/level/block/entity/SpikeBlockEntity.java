package fuzs.spikyspikes.world.level.block.entity;

import fuzs.spikyspikes.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpikeBlockEntity extends BlockEntity {
    public final boolean inventoryRendering;

    public SpikeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        this(p_155229_, p_155230_, false);
    }

    public SpikeBlockEntity(Block block) {
        this(BlockPos.ZERO, block.defaultBlockState(), true);
    }

    private SpikeBlockEntity(BlockPos p_155229_, BlockState p_155230_, boolean inventoryRendering) {
        super(ModRegistry.SPIKE_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
        this.inventoryRendering = inventoryRendering;
    }
}
