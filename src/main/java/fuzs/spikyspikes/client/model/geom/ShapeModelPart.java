package fuzs.spikyspikes.client.model.geom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;

public class ShapeModelPart {

    public static ModelPart pyramid(float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ) {
        return pyramid(0, 0, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, 16.0F, 16.0F);
    }

    public static ModelPart pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float texWidthScaled, float texHeightScaled) {
        return pyramid(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, 0.0F, 0.0F, 0.0F, false, texWidthScaled, texHeightScaled);
    }

    public static ModelPart pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texWidthScaled, float texHeightScaled) {
        return new ModelPart(ImmutableList.of(new ShapeModelPart.Pyramid(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, growX, growY, growZ, mirror, texWidthScaled, texHeightScaled)), ImmutableMap.of());
    }
    
    static class Pyramid extends ModelPart.Cube {
        private final ShapeModelPart.Polygon[] polygons;
        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public Pyramid(int texCoordU, int texCoordV, float minX, float minY, float minZ, float dimensionX, float dimensionY, float dimensionZ, float growX, float growY, float growZ, boolean mirror, float texWidthScaled, float texHeightScaled) {
            super(texCoordU, texCoordV, minX, minY, minZ, dimensionX, dimensionY, dimensionZ, growX, growY, growZ, mirror, texWidthScaled, texHeightScaled);
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = minX + dimensionX;
            this.maxY = minY + dimensionY;
            this.maxZ = minZ + dimensionZ;
            this.polygons = new ShapeModelPart.Polygon[5];
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

            ShapeModelPart.Vertex modelpart$vertex7 = new ShapeModelPart.Vertex(minX, minY, minZ, 0.0F, 0.0F);
            ShapeModelPart.Vertex modelpart$vertex = new ShapeModelPart.Vertex(maxX, minY, minZ, 0.0F, 8.0F);
            ShapeModelPart.Vertex modelpart$vertex1 = new ShapeModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 8.0F);
            ShapeModelPart.Vertex modelpart$vertex2 = new ShapeModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 0.0F);
            ShapeModelPart.Vertex modelpart$vertex3 = new ShapeModelPart.Vertex(minX, minY, maxZ, 0.0F, 0.0F);
            ShapeModelPart.Vertex modelpart$vertex4 = new ShapeModelPart.Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
            ShapeModelPart.Vertex modelpart$vertex5 = new ShapeModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 8.0F);
            ShapeModelPart.Vertex modelpart$vertex6 = new ShapeModelPart.Vertex(centerX, maxY, centerZ, 8.0F, 0.0F);
            this.polygons[2] = new ShapeModelPart.Polygon(new ShapeModelPart.Vertex[]{modelpart$vertex4, modelpart$vertex3, modelpart$vertex7, modelpart$vertex}, texCoordU, texCoordV, texCoordU + dimensionX, texCoordV + dimensionZ, texWidthScaled, texHeightScaled, false, Direction.DOWN);
            this.polygons[1] = new ShapeModelPart.Polygon(new ShapeModelPart.Vertex[]{modelpart$vertex7, modelpart$vertex3, modelpart$vertex6, modelpart$vertex2}, texCoordU, texCoordV, texCoordU + dimensionX, texCoordV + dimensionZ, texWidthScaled, texHeightScaled, false, Direction.WEST);
            this.polygons[3] = new ShapeModelPart.Polygon(new ShapeModelPart.Vertex[]{modelpart$vertex, modelpart$vertex7, modelpart$vertex2, modelpart$vertex1}, texCoordU, texCoordV, texCoordU + dimensionX, texCoordV + dimensionZ, texWidthScaled, texHeightScaled, false, Direction.NORTH);
            this.polygons[0] = new ShapeModelPart.Polygon(new ShapeModelPart.Vertex[]{modelpart$vertex4, modelpart$vertex, modelpart$vertex1, modelpart$vertex5}, texCoordU, texCoordV, texCoordU + dimensionX, texCoordV + dimensionZ, texWidthScaled, texHeightScaled, false, Direction.EAST);
            this.polygons[4] = new ShapeModelPart.Polygon(new ShapeModelPart.Vertex[]{modelpart$vertex3, modelpart$vertex4, modelpart$vertex5, modelpart$vertex6}, texCoordU, texCoordV, texCoordU + dimensionX, texCoordV + dimensionZ, texWidthScaled, texHeightScaled, false, Direction.SOUTH);
        }

        @Override
        public void compile(PoseStack.Pose p_171333_, VertexConsumer p_171334_, int p_171335_, int p_171336_, float p_171337_, float p_171338_, float p_171339_, float p_171340_) {
            Matrix4f matrix4f = p_171333_.pose();
            Matrix3f matrix3f = p_171333_.normal();

            for (ShapeModelPart.Polygon modelpart$polygon : this.polygons) {
                Vector3f vector3f = modelpart$polygon.normal.copy();
                vector3f.transform(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();

                for (ShapeModelPart.Vertex modelpart$vertex : modelpart$polygon.vertices) {
                    float f3 = modelpart$vertex.pos.x() / 16.0F;
                    float f4 = modelpart$vertex.pos.y() / 16.0F;
                    float f5 = modelpart$vertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.transform(matrix4f);
                    p_171334_.vertex(vector4f.x(), vector4f.y(), vector4f.z(), p_171337_, p_171338_, p_171339_, p_171340_, modelpart$vertex.u, modelpart$vertex.v, p_171336_, p_171335_, f, f1, f2);
                }
            }
        }
    }
    
    static class Polygon {
        public final ShapeModelPart.Vertex[] vertices;
        public final Vector3f normal;

        public Polygon(ShapeModelPart.Vertex[] p_104362_, float p_104363_, float p_104364_, float p_104365_, float p_104366_, float p_104367_, float p_104368_, boolean p_104369_, Direction p_104370_) {
            this.vertices = p_104362_;
            float f = 0.0F / p_104367_;
            float f1 = 0.0F / p_104368_;
            p_104362_[0] = p_104362_[0].remap(p_104365_ / p_104367_ - f, p_104364_ / p_104368_ + f1);
            p_104362_[1] = p_104362_[1].remap(p_104363_ / p_104367_ + f, p_104364_ / p_104368_ + f1);
            p_104362_[2] = p_104362_[2].remap(p_104363_ / p_104367_ + f, p_104366_ / p_104368_ - f1);
            p_104362_[3] = p_104362_[3].remap(p_104365_ / p_104367_ - f, p_104366_ / p_104368_ - f1);
            if (p_104369_) {
                int i = p_104362_.length;

                for(int j = 0; j < i / 2; ++j) {
                    ShapeModelPart.Vertex modelpart$vertex = p_104362_[j];
                    p_104362_[j] = p_104362_[i - 1 - j];
                    p_104362_[i - 1 - j] = modelpart$vertex;
                }
            }

            this.normal = p_104370_.step();
            if (p_104369_) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }

        }
    }

    static class Vertex {
        public final Vector3f pos;
        public final float u;
        public final float v;

        public Vertex(float x, float y, float z, float u, float v) {
            this(new Vector3f(x, y, z), u, v);
        }

        public ShapeModelPart.Vertex remap(float u, float v) {
            return new ShapeModelPart.Vertex(this.pos, u, v);
        }

        public Vertex(Vector3f pos, float u, float v) {
            this.pos = pos;
            this.u = u;
            this.v = v;
        }
    }
}
