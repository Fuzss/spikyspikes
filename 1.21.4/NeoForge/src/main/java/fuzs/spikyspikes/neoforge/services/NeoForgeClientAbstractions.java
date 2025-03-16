package fuzs.spikyspikes.neoforge.services;

import fuzs.spikyspikes.services.ClientAbstractions;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateUnbakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class NeoForgeClientAbstractions implements ClientAbstractions {

    @Override
    public UnbakedModel createForwardingUnbakedModel(UnbakedModel unbakedModel, BiFunction<BakedModel, ModelState, BakedModel> bakedModelFinalizer) {
        return new DelegateUnbakedModel(unbakedModel) {
            @Override
            public BakedModel bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
                return bakedModelFinalizer.apply(super.bake(textureSlots,
                        modelBaker,
                        modelState,
                        useAmbientOcclusion,
                        usesBlockLight,
                        itemTransforms,
                        additionalProperties), modelState);
            }
        };
    }

    @Override
    public BakedModel createForwardingBakedModel(BakedModel bakedModel, Map<Direction, List<BakedQuad>> bakedQuadsMap) {
        return new DelegateBakedModel(bakedModel) {
            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource, ModelData modelData, @Nullable RenderType renderType) {
                return bakedQuadsMap.get(direction);
            }
        };
    }
}
