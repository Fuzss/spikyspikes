package fuzs.spikyspikes.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

public class SpikeSpecialRenderer implements NoDataSpecialModelRenderer {
    private final SpikeMaterial spikeMaterial;

    public SpikeSpecialRenderer(SpikeMaterial spikeMaterial) {
        this.spikeMaterial = spikeMaterial;
    }

    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        SpikeRenderer.renderSpike(Direction.UP,
                this.spikeMaterial,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                true,
                hasFoilType);
    }

    public record Unbaked(SpikeMaterial spikeMaterial) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = SpikeMaterial.CODEC.xmap(Unbaked::new, Unbaked::spikeMaterial)
                .fieldOf("spike_material");

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            return new SpikeSpecialRenderer(this.spikeMaterial);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
