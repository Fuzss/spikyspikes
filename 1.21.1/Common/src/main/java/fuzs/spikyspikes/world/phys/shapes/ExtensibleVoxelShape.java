package fuzs.spikyspikes.world.phys.shapes;

import fuzs.spikyspikes.mixin.accessor.VoxelShapeAccessor;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class ExtensibleVoxelShape extends SliceShape {

    public ExtensibleVoxelShape(VoxelShape voxelProvider) {
        super(voxelProvider, Direction.Axis.X, 0);
        ((VoxelShapeAccessor) this).spikyspikes$setShape(((VoxelShapeAccessor) voxelProvider).spikyspikes$getShape());
    }

    @Override
    public abstract DoubleList getCoords(Direction.Axis axis);
}
