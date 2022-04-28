package fuzs.spikyspikes.world.phys.shapes;

import fuzs.spikyspikes.SpikySpikes;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ExtensibleVoxelShape extends SliceShape {

    private final Field partField = ObfuscationReflectionHelper.findField(VoxelShape.class, "f_83211_");
    private final Method getValuesMethod = ObfuscationReflectionHelper.findMethod(VoxelShape.class, "m_7700_", Direction.Axis.class);

    public ExtensibleVoxelShape(VoxelShape voxelProvider) {

        super(voxelProvider, Direction.Axis.X, 0);
        this.setVoxelPart(this, this.getVoxelPart(voxelProvider));
    }

    @Override
    protected abstract DoubleList getCoords(Direction.Axis axis);

    protected final void setVoxelPart(VoxelShape voxelShape, DiscreteVoxelShape part) {

        try {

            this.partField.set(voxelShape, part);
        } catch (IllegalAccessException ignored) {

            SpikySpikes.LOGGER.warn("Unable to set part field in {}", voxelShape.getClass().toString());
        }
    }

    protected final DiscreteVoxelShape getVoxelPart(VoxelShape voxelShape) {

        try {

            return (DiscreteVoxelShape) this.partField.get(voxelShape);
        } catch (IllegalAccessException ignored) {

            SpikySpikes.LOGGER.warn("Unable to get part field in {}", voxelShape.getClass().toString());
        }

        return null;
    }

    protected final DoubleList callGetValues(VoxelShape voxelShape, Direction.Axis axis) {

        try {

            return (DoubleList) this.getValuesMethod.invoke(voxelShape, axis);
        } catch (IllegalAccessException | InvocationTargetException ignored) {

            SpikySpikes.LOGGER.warn("Unable to call 'getValues' method in {}", voxelShape.getClass().toString());
        }

        return null;
    }

}
