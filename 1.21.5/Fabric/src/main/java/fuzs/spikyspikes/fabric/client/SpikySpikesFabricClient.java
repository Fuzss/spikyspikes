package fuzs.spikyspikes.fabric.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.client.SpikySpikesClient;
import fuzs.spikyspikes.client.renderer.block.model.SpikeModelGenerator;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedModelDeserializer;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperUnbakedModel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.UnbakedGeometry;
import net.minecraft.client.resources.model.UnbakedModel;

public class SpikySpikesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(SpikySpikes.MOD_ID, SpikySpikesClient::new);
        UnbakedModelDeserializer.register(SpikeModelGenerator.BUILTIN_SPIKE_MODEL,
                (JsonObject jsonObject, JsonDeserializationContext context) -> {
                    // https://docs.neoforged.net/docs/resources/client/models/modelloaders/#reusing-the-default-model-loader
                    jsonObject.remove("fabric:type");
                    UnbakedModel unbakedModel = context.deserialize(jsonObject, UnbakedModel.class);
                    return new WrapperUnbakedModel() {
                        {
                            this.wrapped = unbakedModel;
                        }

                        @Override
                        public TextureSlots.Data textureSlots() {
                            return SpikeModelGenerator.TEXTURE_SLOTS;
                        }

                        @Override
                        public UnbakedGeometry geometry() {
                            return SpikeModelGenerator::bake;
                        }
                    };
                });
    }
}
