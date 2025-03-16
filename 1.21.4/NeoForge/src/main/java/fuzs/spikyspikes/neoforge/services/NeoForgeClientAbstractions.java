package fuzs.spikyspikes.neoforge.services;

import fuzs.spikyspikes.services.ClientAbstractions;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class NeoForgeClientAbstractions implements ClientAbstractions {

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
