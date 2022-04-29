package fuzs.spikyspikes.world.phys.shapes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class CustomOutlineShape extends ExtensibleVoxelShape {
    private final VoxelShape mainShape;
    private final List<Vec3[]> outlineShapeEdges;

    public CustomOutlineShape(VoxelShape mainShape, Vec3... outlineShapeEdges) {
        super(mainShape);
        this.mainShape = mainShape;
        this.outlineShapeEdges = createOutlineList(outlineShapeEdges);
    }

    private static List<Vec3[]> createOutlineList(Vec3[] outlineShapeEdges) {
        if (outlineShapeEdges.length % 2 != 0) {
            throw new IllegalArgumentException("Edges must be in groups of two corners");
        }
        List<Vec3[]> list = Lists.newArrayList();
        for (int i = 0; i < outlineShapeEdges.length; i += 2) {
            list.add(new Vec3[]{outlineShapeEdges[i], outlineShapeEdges[i + 1]});
        }
        return list;
    }

    @Override
    protected DoubleList getCoords(Direction.Axis axis) {
        return this.callGetValues(this.mainShape, axis);
    }

    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        for (Vec3[] edge : this.outlineShapeEdges) {
            boxConsumer.consume(edge[0].x, edge[0].y, edge[0].z, edge[1].x, edge[1].y, edge[1].z);
        }
    }
}
