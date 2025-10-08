package fuzs.spikyspikes.client.renderer.block.model;

import com.mojang.math.Quadrant;
import fuzs.puzzleslib.api.client.renderer.v1.model.QuadUtils;
import fuzs.spikyspikes.SpikySpikes;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class SpikeModelGenerator implements UnbakedModel {
    public static final ResourceLocation BUILTIN_SPIKE_MODEL = SpikySpikes.id("builtin/spike");
    /**
     * The upper face is only removed after baking and will log a missing texture warning if not present.
     */
    public static final TextureSlots.Data TEXTURE_SLOTS = new TextureSlots.Data.Builder().addTexture(Direction.UP.getSerializedName(),
            new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation())).build();
    private static final List<BlockElement> ELEMENTS = Collections.singletonList(createCubeElement());

    @Override
    public TextureSlots.Data textureSlots() {
        return TEXTURE_SLOTS;
    }

    @Override
    public UnbakedGeometry geometry() {
        return SpikeModelGenerator::bake;
    }

    private static BlockElement createCubeElement() {
        BlockElementFace.UVs blockFaceUV = new BlockElementFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
        Map<Direction, BlockElementFace> map = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            map.put(direction,
                    new BlockElementFace(direction, -1, direction.getSerializedName(), blockFaceUV, Quadrant.R0));
        }
        return new BlockElement(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), map);
    }

    public static QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
        QuadCollection quadCollection = SimpleUnbakedGeometry.bake(ELEMENTS,
                textureSlots,
                modelBaker.sprites(),
                modelState,
                modelDebugName);
        return modifyBakedModel(quadCollection, modelState, SpikeModelGenerator::finalizeBakedQuad);
    }

    private static QuadCollection modifyBakedModel(QuadCollection quadCollection, ModelState modelState, BakedQuadFinalizer bakedQuadFinalizer) {
        Map<Direction, BakedQuad> bakedQuadMap = Util.makeEnumMap(Direction.class,
                (Direction direction) -> quadCollection.getQuads(direction).getFirst());
        QuadCollection.Builder builder = new QuadCollection.Builder();
        for (BakedQuad bakedQuad : quadCollection.getQuads(null)) {
            builder.addUnculledFace(bakedQuad);
        }
        Function<Direction, Direction> directionRotator = Util.memoize((Direction direction) -> {
            return Direction.rotate(modelState.transformation().getMatrix(), direction);
        });
        for (Map.Entry<Direction, BakedQuad> entry : bakedQuadMap.entrySet()) {
            bakedQuadFinalizer.finalizeBakedQuad(Direction.rotate(modelState.transformation().getMatrixCopy().invert(),
                            entry.getKey()),
                    entry.getValue(),
                    directionRotator::apply,
                    (@Nullable Direction direction, BakedQuad bakedQuad) -> {
                        if (direction != null) {
                            builder.addCulledFace(direction, bakedQuad);
                        } else {
                            builder.addUnculledFace(bakedQuad);
                        }
                    });
        }
        return builder.build();
    }

    private static void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<@Nullable Direction, BakedQuad> bakedQuadConsumer) {
        if (direction != Direction.UP) {
            bakedQuad = QuadUtils.copy(bakedQuad);
            if (direction.getAxis().isHorizontal()) {
                int[] maxVertexIndices;
                if (directionRotator.apply(Direction.UP).getAxisDirection() == Direction.UP.getAxisDirection()) {
                    maxVertexIndices = getMaxVertexIndices(bakedQuad, directionRotator.apply(Direction.UP).getAxis());
                } else {
                    maxVertexIndices = getMinVertexIndices(bakedQuad, directionRotator.apply(Direction.UP).getAxis());
                }
                for (int vertexIndex : maxVertexIndices) {
                    setQuadPosition(bakedQuad, vertexIndex, directionRotator.apply(Direction.EAST).getAxis(), 0.5F);
                    setQuadPosition(bakedQuad, vertexIndex, directionRotator.apply(Direction.SOUTH).getAxis(), 0.5F);
                    float u0 = QuadUtils.getU(bakedQuad, maxVertexIndices[0]);
                    float u1 = QuadUtils.getU(bakedQuad, maxVertexIndices[1]);
                    QuadUtils.setU(bakedQuad, vertexIndex, Mth.lerp(0.5F, u0, u1));
                }
                QuadUtils.fillNormal(bakedQuad);
                bakedQuadConsumer.accept(null, bakedQuad);
            } else {
                bakedQuadConsumer.accept(directionRotator.apply(direction), bakedQuad);
            }
        }
    }

    public static int[] getMaxVertexIndices(BakedQuad bakedQuad, Direction.Axis axis) {
        IntList maxVertexIndices = new IntArrayList();
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            float positionComponent = getQuadPosition(bakedQuad, i, axis);
            if (positionComponent > maxValue) {
                maxVertexIndices.clear();
                maxValue = positionComponent;
            }
            if (positionComponent == maxValue) {
                maxVertexIndices.add(i);
            }
        }
        return maxVertexIndices.toIntArray();
    }

    public static int[] getMinVertexIndices(BakedQuad bakedQuad, Direction.Axis axis) {
        IntList minVertexIndices = new IntArrayList();
        float minValue = Float.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            float positionComponent = getQuadPosition(bakedQuad, i, axis);
            if (positionComponent < minValue) {
                minVertexIndices.clear();
                minValue = positionComponent;
            }
            if (positionComponent == minValue) {
                minVertexIndices.add(i);
            }
        }
        return minVertexIndices.toIntArray();
    }

    public static float getQuadPosition(BakedQuad bakedQuad, int vertexIndex, Direction.Axis axis) {
        Vector3f vector3f = QuadUtils.getPosition(bakedQuad, vertexIndex);
        return (float) axis.choose(vector3f.x(), vector3f.y(), vector3f.z());
    }

    public static void setQuadPosition(BakedQuad bakedQuad, int vertexIndex, Direction.Axis axis, float positionComponent) {
        Vector3f vector3f = QuadUtils.getPosition(bakedQuad, vertexIndex);
        vector3f.sub(vector3f.mul(axis.getPositive().step(), new Vector3f()));
        QuadUtils.setPosition(bakedQuad, vertexIndex, vector3f.add(axis.getPositive().step().mul(positionComponent)));
    }

    @FunctionalInterface
    public interface BakedQuadFinalizer {

        void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<@Nullable Direction, BakedQuad> bakedQuadConsumer);
    }
}
