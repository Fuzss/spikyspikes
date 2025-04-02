package fuzs.spikyspikes.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.spikyspikes.client.renderer.block.model.SpikeModelGenerator;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelManager.class)
abstract class ModelManagerFabricMixin {

    @Inject(
            method = "discoverModelDependencies",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/block/model/ItemModelGenerator;GENERATED_ITEM_MODEL_ID:Lnet/minecraft/resources/ResourceLocation;"
            )
    )
    private static void discoverModelDependencies(CallbackInfoReturnable<Object> callback, @Local ModelDiscovery modelDiscovery) {
        modelDiscovery.addSpecialModel(SpikeModelGenerator.BUILTIN_SPIKE_MODEL, new SpikeModelGenerator());
    }
}
