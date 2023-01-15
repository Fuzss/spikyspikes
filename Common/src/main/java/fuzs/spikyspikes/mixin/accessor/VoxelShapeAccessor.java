package fuzs.spikyspikes.mixin.accessor;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VoxelShape.class)
public interface VoxelShapeAccessor {

    @Accessor("shape")
    DiscreteVoxelShape spikyspikes$getShape();

    @Accessor("shape")
    @Mutable
    void spikyspikes$setShape(DiscreteVoxelShape shape);

    @Invoker("getCoords")
    DoubleList spikyspikes$getCoords(Direction.Axis axis);
}
