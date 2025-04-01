package fuzs.spikyspikes.client.renderer.block.model;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.util.QuadUtils;
import fuzs.spikyspikes.services.ClientAbstractions;
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
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class SpikeModelGenerator implements UnbakedModel {
    public static final ResourceLocation BUILTIN_SPIKE_MODEL = SpikySpikes.id("builtin/spike");
    private static final TextureSlots.Data TEXTURE_SLOTS = new TextureSlots.Data.Builder().addTexture(Direction.UP.getSerializedName(),
            new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation())).build();
    private static final List<BlockElement> ELEMENTS = Collections.singletonList(createCubeElement());

    @Override
    public TextureSlots.Data getTextureSlots() {
        // the upper face is only removed after baking and will log a missing texture warning if not present
        return TEXTURE_SLOTS;
    }

    @Override
    public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
        BakedModel bakedModel = SimpleBakedModel.bakeElements(ELEMENTS,
                textureSlots,
                baker.sprites(),
                modelState,
                hasAmbientOcclusion,
                useBlockLight,
                true,
                transforms);
        return this.modifyBakedModel(bakedModel, modelState, this::finalizeBakedQuad);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        // NO-OP
    }

    private static BlockElement createCubeElement() {
        BlockFaceUV blockFaceUV = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        Map<Direction, BlockElementFace> map = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            map.put(direction, new BlockElementFace(direction, -1, direction.getSerializedName(), blockFaceUV));
        }
        return new BlockElement(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), map);
    }

    private BakedModel modifyBakedModel(BakedModel bakedModel, ModelState modelState, BakedQuadFinalizer bakedQuadFinalizer) {
        Map<Direction, BakedQuad> bakedQuadMap = Util.makeEnumMap(Direction.class,
                (Direction direction) -> bakedModel.getQuads(null, direction, RandomSource.create()).getFirst());
        Map<Direction, List<BakedQuad>> bakedQuadsMap = Util.make(new HashMap<>(Util.makeEnumMap(Direction.class,
                        (Direction direction) -> new ArrayList<>())),
                (Map<Direction, List<BakedQuad>> map) -> map.put(null,
                        new ArrayList<>(bakedModel.getQuads(null, null, RandomSource.create()))));
        Function<Direction, Direction> directionRotator = Util.memoize((Direction direction) -> {
            return Direction.rotate(modelState.getRotation().getMatrix(), direction);
        });
        for (Map.Entry<Direction, BakedQuad> entry : bakedQuadMap.entrySet()) {
            bakedQuadFinalizer.finalizeBakedQuad(Direction.rotate(modelState.getRotation().getMatrix().invert(),
                            entry.getKey()),
                    entry.getValue(),
                    directionRotator::apply,
                    (Direction direction, BakedQuad bakedQuad) -> {
                        bakedQuadsMap.get(direction).add(bakedQuad);
                    });
        }
        return ClientAbstractions.INSTANCE.createForwardingBakedModel(bakedModel, bakedQuadsMap);
    }

    protected void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<Direction, BakedQuad> bakedQuadConsumer) {
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

        void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<Direction, BakedQuad> bakedQuadConsumer);
    }
}
