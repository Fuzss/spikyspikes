package fuzs.spikyspikes.client.handler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.spikyspikes.client.util.InMemoryBlockModelHelper;
import fuzs.spikyspikes.client.util.QuadUtils;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.services.ClientAbstractions;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class SpikeBlockModelHandler {
    static final Map<ResourceLocation, Map<Direction, ResourceLocation>> SPIKE_BLOCK_TEXTURE_MAPPINGS;

    static {
        ImmutableMap.Builder<ResourceLocation, Map<Direction, ResourceLocation>> builder = ImmutableMap.builder();
        registerSpikeBlock(ModRegistry.WOODEN_SPIKE_BLOCK.value(), (Direction direction) -> {
            return direction == Direction.DOWN ? ModelLocationHelper.getBlockTexture(Blocks.STRIPPED_OAK_LOG, "_top") :
                    ModelLocationHelper.getBlockTexture(Blocks.STRIPPED_OAK_LOG);
        }, builder::put);
        registerSpikeBlock(ModRegistry.STONE_SPIKE_BLOCK.value(), Blocks.SMOOTH_STONE, builder::put);
        registerSpikeBlock(ModRegistry.IRON_SPIKE_BLOCK.value(), Blocks.IRON_BLOCK, builder::put);
        registerSpikeBlock(ModRegistry.GOLDEN_SPIKE_BLOCK.value(), Blocks.GOLD_BLOCK, builder::put);
        registerSpikeBlock(ModRegistry.DIAMOND_SPIKE_BLOCK.value(), Blocks.DIAMOND_BLOCK, builder::put);
        registerSpikeBlock(ModRegistry.NETHERITE_SPIKE_BLOCK.value(), Blocks.NETHERITE_BLOCK, builder::put);
        SPIKE_BLOCK_TEXTURE_MAPPINGS = builder.build();
    }

    static void registerSpikeBlock(Block spikeBlock, Block textureBlock, BiConsumer<ResourceLocation, Map<Direction, ResourceLocation>> builder) {
        ResourceLocation resourceLocation = ModelLocationHelper.getBlockTexture(textureBlock);
        registerSpikeBlock(spikeBlock, (Direction direction) -> resourceLocation, builder);
    }

    static void registerSpikeBlock(Block spikeBlock, Function<Direction, ResourceLocation> textureGetter, BiConsumer<ResourceLocation, Map<Direction, ResourceLocation>> builder) {
        builder.accept(ModelLocationHelper.getBlockModel(spikeBlock),
                Maps.immutableEnumMap(Util.makeEnumMap(Direction.class, textureGetter)));
    }

    public static EventResultHolder<UnbakedModel> onLoadModel(ResourceLocation resourceLocation, @Nullable UnbakedModel unbakedModel) {
        if (unbakedModel instanceof BlockModel blockModel &&
                SPIKE_BLOCK_TEXTURE_MAPPINGS.containsKey(resourceLocation)) {
            UnbakedModel newUnbakedModel = InMemoryBlockModelHelper.createCubeModel(blockModel,
                    SPIKE_BLOCK_TEXTURE_MAPPINGS.get(resourceLocation));
            newUnbakedModel = ClientAbstractions.INSTANCE.createForwardingUnbakedModel(newUnbakedModel,
                    (BakedModel bakedModel, ModelState modelState) -> {
                        return InMemoryBlockModelHelper.modifyBakedModel(bakedModel,
                                modelState,
                                SpikeBlockModelHandler::finalizeBakedQuad);
                    });
            return EventResultHolder.interrupt(newUnbakedModel);
        } else {
            return EventResultHolder.pass();
        }
    }

    static void finalizeBakedQuad(Direction direction, BakedQuad bakedQuad, UnaryOperator<Direction> directionRotator, BiConsumer<Direction, BakedQuad> bakedQuadConsumer) {
        if (direction != Direction.UP) {
            bakedQuad = QuadUtils.copy(bakedQuad);
            if (direction.getAxis().isHorizontal()) {
                int[] maxVertexIndices;
                if (directionRotator.apply(Direction.UP).getAxisDirection() == Direction.UP.getAxisDirection()) {
                    maxVertexIndices = SpikeBlockModelHandler.getMaxVertexIndices(bakedQuad,
                            directionRotator.apply(Direction.UP).getAxis());
                } else {
                    maxVertexIndices = SpikeBlockModelHandler.getMinVertexIndices(bakedQuad,
                            directionRotator.apply(Direction.UP).getAxis());
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
}
