package fuzs.spikyspikes.fabric.services;

import fuzs.spikyspikes.services.ClientAbstractions;
import net.fabricmc.fabric.api.client.model.loading.v1.WrapperUnbakedModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.renderer.VanillaModelEncoder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public UnbakedModel createForwardingUnbakedModel(UnbakedModel unbakedModel, BiFunction<BakedModel, ModelState, BakedModel> bakedModelFinalizer) {
        return new WrapperUnbakedModel(unbakedModel) {
            @Override
            public BakedModel bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, boolean ambientOcclusion, boolean isSideLit, ItemTransforms itemTransforms) {
                return bakedModelFinalizer.apply(super.bake(textureSlots,
                        modelBaker,
                        modelState,
                        ambientOcclusion,
                        isSideLit,
                        itemTransforms), modelState);
            }
        };
    }

    @Override
    public BakedModel createForwardingBakedModel(BakedModel bakedModel, Map<Direction, List<BakedQuad>> bakedQuadsMap) {
        return new DelegateBakedModel(bakedModel) {
            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
                return bakedQuadsMap.get(direction);
            }

            @Override
            public void emitBlockQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, Predicate<@Nullable Direction> cullTest) {
                VanillaModelEncoder.emitBlockQuads(emitter, this, state, randomSupplier, cullTest);
            }

            @Override
            public void emitItemQuads(QuadEmitter emitter, Supplier<RandomSource> randomSupplier) {
                VanillaModelEncoder.emitItemQuads(emitter, this, null, randomSupplier);
            }
        };
    }
}
