package fuzs.spikyspikes.client.util;

import fuzs.spikyspikes.services.ClientAbstractions;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Partially adapted from {@link net.minecraft.client.resources.model.MissingBlockModel}.
 */
public final class InMemoryBlockModelHelper {

    private InMemoryBlockModelHelper() {
        // NO-OP
    }

    public static UnbakedModel createCubeModel(BlockModel blockModel, ResourceLocation resourceLocation) {
        return createCubeModel(blockModel, Util.makeEnumMap(Direction.class, (Direction direction) -> {
            return resourceLocation;
        }));
    }

    public static UnbakedModel createCubeModel(BlockModel blockModel, Map<Direction, ResourceLocation> textureMappings) {
        TextureSlots.Data.Builder builder = copyTextureSlots(blockModel.getTextureSlots());
        for (Direction direction : Direction.values()) {
            builder.addTexture(direction.getSerializedName(),
                    new Material(TextureAtlas.LOCATION_BLOCKS,
                            textureMappings.getOrDefault(direction, MissingTextureAtlasSprite.getLocation())));
        }
        return new BlockModel(blockModel.getParentLocation(),
                Collections.singletonList(createCubeElement()),
                builder.build(),
                false,
                UnbakedModel.GuiLight.FRONT,
                blockModel.getTransforms());
    }

    private static BlockElement createCubeElement() {
        BlockFaceUV blockFaceUV = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
        Map<Direction, BlockElementFace> map = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            map.put(direction, new BlockElementFace(direction, -1, direction.getSerializedName(), blockFaceUV));
        }
        return new BlockElement(new Vector3f(0.0F, 0.0F, 0.0F), new Vector3f(16.0F, 16.0F, 16.0F), map);
    }

    private static TextureSlots.Data.Builder copyTextureSlots(TextureSlots.Data textureSlots) {
        TextureSlots.Data.Builder builder = new TextureSlots.Data.Builder();
        for (Map.Entry<String, TextureSlots.SlotContents> entry : textureSlots.values().entrySet()) {
            if (entry.getValue() instanceof TextureSlots.Reference(String target)) {
                builder.addReference(entry.getKey(), target);
            } else if (entry.getValue() instanceof TextureSlots.Value(Material material)) {
                builder.addTexture(entry.getKey(), material);
            }
        }
        return builder;
    }

    public static BakedModel modifyBakedModel(BakedModel bakedModel, ModelState modelState, InMemoryBlockModelHelper.BakedQuadFinalizer bakedQuadFinalizer) {
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

    @FunctionalInterface
    public interface BakedQuadFinalizer {

        void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<Direction, BakedQuad> bakedQuadConsumer);
    }
}
