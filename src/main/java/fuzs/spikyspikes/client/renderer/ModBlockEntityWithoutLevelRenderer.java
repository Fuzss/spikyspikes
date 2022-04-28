package fuzs.spikyspikes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.spikyspikes.client.renderer.blockentity.SpikeRenderer;
import fuzs.spikyspikes.world.item.SpikeItem;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

public class ModBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

    public ModBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
    }

    @Override
    public void renderByItem(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
        SpikeBlock block = ((SpikeItem) p_108830_.getItem()).getBlock();
        SpikeRenderer.renderSpike(Direction.UP, block.spikeMaterial, p_108832_, p_108833_, p_108834_, p_108835_, true, p_108830_.hasFoil());
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_172555_) {

    }

    public static IItemRenderProperties createItemRenderProperties() {
        return new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                Minecraft minecraft = Minecraft.getInstance();
                return new ModBlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
            }
        };
    }
}
