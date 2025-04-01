package fuzs.spikyspikes.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

/**
 * Similar to NeoForge's {@code net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer} and Fabric's
 * {@code net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView}.
 */
public final class QuadUtils {
    public static final int VERTEX_STRIDE = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
    public static final int VERTEX_POSITION = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.POSITION) / 4;
    public static final int VERTEX_COLOR = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.COLOR) / 4;
    public static final int VERTEX_UV = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.UV) / 4;
    public static final int VERTEX_UV0 = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.UV0) / 4;
    public static final int VERTEX_UV1 = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.UV1) / 4;
    public static final int VERTEX_UV2 = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.UV2) / 4;
    public static final int VERTEX_NORMAL = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.NORMAL) / 4;

    private QuadUtils() {
        // NO-OP
    }

    public static BakedQuad copy(BakedQuad bakedQuad) {
        return ClientAbstractions.INSTANCE.copyBakedQuad(bakedQuad);
    }

    public static Vector3f getPosition(BakedQuad bakedQuad, int vertexIndex) {
        return new Vector3f(getX(bakedQuad, vertexIndex), getY(bakedQuad, vertexIndex), getZ(bakedQuad, vertexIndex));
    }

    public static float getX(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        return Float.intBitsToFloat(bakedQuad.getVertices()[offset]);
    }

    public static float getY(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        return Float.intBitsToFloat(bakedQuad.getVertices()[offset + 1]);
    }

    public static float getZ(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        return Float.intBitsToFloat(bakedQuad.getVertices()[offset + 2]);
    }

    public static Vector3f getNormal(BakedQuad bakedQuad, int vertexIndex) {
        return new Vector3f(getNormalX(bakedQuad, vertexIndex),
                getNormalY(bakedQuad, vertexIndex),
                getNormalZ(bakedQuad, vertexIndex));
    }

    public static float getNormalX(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        int normal = bakedQuad.getVertices()[offset];
        return ((byte) (normal & 0xFF)) / 127.0F;
    }

    public static float getNormalY(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        int normal = bakedQuad.getVertices()[offset];
        return ((byte) ((normal >> 8) & 0xFF)) / 127.0F;
    }

    public static float getNormalZ(BakedQuad bakedQuad, int vertexIndex) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        int normal = bakedQuad.getVertices()[offset];
        return ((byte) ((normal >> 16) & 0xFF)) / 127.0F;
    }

    public static int getColor(BakedQuad bakedQuad, int vertexIndex) {
        return bakedQuad.getVertices()[vertexIndex * VERTEX_STRIDE + VERTEX_COLOR];
    }

    public static Vector2f getUv(BakedQuad bakedQuad, int vertexIndex) {
        return new Vector2f(getU(bakedQuad, vertexIndex), getV(bakedQuad, vertexIndex));
    }

    public static float getU(BakedQuad bakedQuad, int vertexIndex) {
        return Float.intBitsToFloat(bakedQuad.getVertices()[vertexIndex * VERTEX_STRIDE + VERTEX_UV]);
    }

    public static float getV(BakedQuad bakedQuad, int vertexIndex) {
        return Float.intBitsToFloat(bakedQuad.getVertices()[vertexIndex * VERTEX_STRIDE + VERTEX_UV + 1]);
    }

    public static void setPosition(BakedQuad bakedQuad, int vertexIndex, Vector3f position) {
        setPosition(bakedQuad, vertexIndex, position.x, position.y, position.z);
    }

    public static void setPosition(BakedQuad bakedQuad, int vertexIndex, float x, float y, float z) {
        setX(bakedQuad, vertexIndex, x);
        setY(bakedQuad, vertexIndex, y);
        setZ(bakedQuad, vertexIndex, z);
    }

    public static void setX(BakedQuad bakedQuad, int vertexIndex, float x) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        bakedQuad.getVertices()[offset] = Float.floatToRawIntBits(x);
    }

    public static void setY(BakedQuad bakedQuad, int vertexIndex, float y) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        bakedQuad.getVertices()[offset + 1] = Float.floatToRawIntBits(y);
    }

    public static void setZ(BakedQuad bakedQuad, int vertexIndex, float z) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_POSITION;
        bakedQuad.getVertices()[offset + 2] = Float.floatToRawIntBits(z);
    }

    public static void setNormal(BakedQuad bakedQuad, int vertexIndex, Vector3f normal) {
        setNormal(bakedQuad, vertexIndex, normal.x, normal.y, normal.z);
    }

    public static void setNormal(BakedQuad bakedQuad, int vertexIndex, float x, float y, float z) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        bakedQuad.getVertices()[offset] =
                ((int) (x * 127.0f) & 0xFF) | (((int) (y * 127.0f) & 0xFF) << 8) | (((int) (z * 127.0f) & 0xFF) << 16);
    }

    public static void setColor(BakedQuad bakedQuad, int vertexIndex, int r, int g, int b, int a) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_COLOR;
        bakedQuad.getVertices()[offset] = ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((g & 0xFF) << 8) | (r & 0xFF);
    }

    public static void setUv(BakedQuad bakedQuad, int vertexIndex, Vector2f uv) {
        setUv(bakedQuad, vertexIndex, uv.x, uv.y);
    }

    public static void setUv(BakedQuad bakedQuad, int vertexIndex, float u, float v) {
        setU(bakedQuad, vertexIndex, u);
        setU(bakedQuad, vertexIndex, v);
    }

    public static void setU(BakedQuad bakedQuad, int vertexIndex, float u) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_UV;
        bakedQuad.getVertices()[offset] = Float.floatToRawIntBits(u);
    }

    public static void setV(BakedQuad bakedQuad, int vertexIndex, float v) {
        int offset = vertexIndex * VERTEX_STRIDE + VERTEX_UV;
        bakedQuad.getVertices()[offset + 1] = Float.floatToRawIntBits(v);
    }

    public static void fillNormal(BakedQuad bakedQuad) {
        Vector3f v0 = getPosition(bakedQuad, 0);
        Vector3f v1 = getPosition(bakedQuad, 1);
        Vector3f v2 = getPosition(bakedQuad, 2);
        Vector3f v3 = getPosition(bakedQuad, 3);
        v3.sub(v1);
        v2.sub(v0);
        v2.cross(v3);
        v2.normalize();
        for (int i = 0; i < 4; i++) {
            setNormal(bakedQuad, i, v2);
        }
    }
}
