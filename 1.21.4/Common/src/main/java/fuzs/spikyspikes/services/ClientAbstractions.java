package fuzs.spikyspikes.services;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public interface ClientAbstractions {
    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    UnbakedModel createForwardingUnbakedModel(UnbakedModel unbakedModel, BiFunction<BakedModel, ModelState, BakedModel> bakedModelFinalizer);

    BakedModel createForwardingBakedModel(BakedModel bakedModel, Map<Direction, List<BakedQuad>> bakedQuadsMap);
}
