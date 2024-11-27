package fuzs.spikyspikes.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ShapeModelPart {

    public static ModelPartCopy pyramid(float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, boolean fullBrightness) {
        return pyramid(0, 0, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, 16.0F, 16.0F, fullBrightness);
    }

    public static ModelPartCopy pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float texWidthScaled, float texHeightScaled, boolean fullBrightness) {
        return pyramid(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, 0.0F, 0.0F, 0.0F, false, texWidthScaled, texHeightScaled, fullBrightness);
    }

    public static ModelPartCopy pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texWidthScaled, float texHeightScaled, boolean fullBrightness) {
        return new ModelPartCopy(
                List.of(new ShapeModelPart.Pyramid(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, growX, growY, growZ, mirror, texWidthScaled, texHeightScaled, fullBrightness)), Collections.emptyMap());
    }
    
    static class Pyramid extends ModelPart.Cube {
        private final ModelPart.Polygon[] polygons;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texWidthScaled, float texHeightScaled, boolean fullBrightness) {
            super(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, growX, growY, growZ, mirror, texWidthScaled, texHeightScaled, EnumSet.allOf(Direction.class));
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = minX + dimensionX;
            this.maxY = minY + dimensionY;
            this.maxZ = minZ + dimensionZ;
            this.polygons = new ModelPart.Polygon[5];
            float maxX = minX + dimensionX;
            float maxY = minY + dimensionY;
            float maxZ = minZ + dimensionZ;
            float centerX = minX + dimensionX / 2.0F;
            float centerY = minY + dimensionY / 2.0F;
            float centerZ = minZ + dimensionZ / 2.0F;
            minX -= growX;
            minY -= growY;
            minZ -= growZ;
            maxX += growX;
            maxY += growY;
            maxZ += growZ;
            if (mirror) {
                float f3 = maxX;
                maxX = minX;
                minX = f3;
            }

            ModelPart.Vertex modelpart$vertex7 = new ModelPart.Vertex(minX, minY, minZ, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex = new ModelPart.Vertex(maxX, minY, minZ, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex1 = new ModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex2 = new ModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex3 = new ModelPart.Vertex(minX, minY, maxZ, 0.0F, 0.0F);
            ModelPart.Vertex modelpart$vertex4 = new ModelPart.Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex5 = new ModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 8.0F);
            ModelPart.Vertex modelpart$vertex6 = new ModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 0.0F);
            this.polygons[2] = new ModelPart.Polygon(new ModelPart.Vertex[]{modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex}, texCoordU + dimensionX, texCoordV + dimensionZ, texCoordU, texCoordV, texWidthScaled, texHeightScaled, mirror, Direction.DOWN);
            this.polygons[1] = new ModelPart.Polygon(new ModelPart.Vertex[]{modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2}, texCoordU + dimensionX, texCoordV + dimensionZ, texCoordU, texCoordV, texWidthScaled, texHeightScaled, mirror, fullBrightness ? Direction.UP : Direction.WEST);
            this.polygons[3] = new ModelPart.Polygon(new ModelPart.Vertex[]{modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1}, texCoordU + dimensionX, texCoordV + dimensionZ, texCoordU, texCoordV, texWidthScaled, texHeightScaled, mirror, fullBrightness ? Direction.UP : Direction.NORTH);
            this.polygons[0] = new ModelPart.Polygon(new ModelPart.Vertex[]{modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5}, texCoordU + dimensionX, texCoordV + dimensionZ, texCoordU, texCoordV, texWidthScaled, texHeightScaled, mirror, fullBrightness ? Direction.UP : Direction.EAST);
            this.polygons[4] = new ModelPart.Polygon(new ModelPart.Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6}, texCoordU + dimensionX, texCoordV + dimensionZ, texCoordU, texCoordV, texWidthScaled, texHeightScaled, mirror, fullBrightness ? Direction.UP : Direction.SOUTH);
        }

        @Override
        public void compile(PoseStack.Pose pose, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            
            for (ModelPart.Polygon polygon : this.polygons) {
                Vector3f vector3f = matrix3f.transform(new Vector3f(polygon.normal()));
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for (ModelPart.Vertex vertex : polygon.vertices()) {
                    float f3 = vertex.pos().x() / 16.0F;
                    float f4 = vertex.pos().y() / 16.0F;
                    float f5 = vertex.pos().z() / 16.0F;
                    Vector4f vector4f = matrix4f.transform(new Vector4f(f3, f4, f5, 1.0F));
                    buffer.addVertex(vector4f.x(), vector4f.y(), vector4f.z(), color, vertex.u(), vertex.v(), packedOverlay, packedLight, f, f1, f2);
                }
            }
        }
    }
}
