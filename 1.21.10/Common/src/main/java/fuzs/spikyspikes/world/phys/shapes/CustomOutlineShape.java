package fuzs.spikyspikes.world.phys.shapes;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class CustomOutlineShape extends VoxelShape {
    private final VoxelShape collisionShape;
    private final VoxelShape particleShape;
    private final VoxelShape outlineShapeBase;
    private final List<Vec3[]> outlineShapeEdges;

    public CustomOutlineShape(VoxelShape collisionShape, Vec3... outlineShapeEdges) {
        this(collisionShape, Shapes.empty(), outlineShapeEdges);
    }

    public CustomOutlineShape(VoxelShape collisionShape, VoxelShape outlineShapeBase, Vec3... outlineShapeEdges) {
        super(collisionShape.shape);
        this.collisionShape = collisionShape;
        this.particleShape = collisionShape.singleEncompassing();
        this.outlineShapeBase = outlineShapeBase;
        this.outlineShapeEdges = this.createOutlineList(outlineShapeEdges);
    }

    private List<Vec3[]> createOutlineList(Vec3[] outlineShapeEdges) {
        if (outlineShapeEdges.length % 2 != 0) throw new IllegalStateException("Edges must be in groups of two points");
        List<Vec3[]> list = Lists.newArrayList();
        for (int i = 0; i < outlineShapeEdges.length; i += 2) {
            list.add(new Vec3[]{outlineShapeEdges[i], outlineShapeEdges[i + 1]});
        }
        return list;
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return this.collisionShape.getCoords(axis);
    }

    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        this.outlineShapeBase.forAllEdges(boxConsumer);
        for (Vec3[] edge : this.outlineShapeEdges) {
            boxConsumer.consume(edge[0].x, edge[0].y, edge[0].z, edge[1].x, edge[1].y, edge[1].z);
        }
    }

    public void forAllParticleBoxes(Shapes.DoubleLineConsumer doubleLineConsumer) {
        this.particleShape.forAllBoxes(doubleLineConsumer);
    }
}
