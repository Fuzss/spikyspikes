package fuzs.spikyspikes.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

public class ModBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final SpikeBlockEntity blockEntity;

    private ModBlockEntityWithoutLevelRenderer(Block block, BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        this.blockEntityRenderDispatcher = p_172550_;
        this.blockEntity = new SpikeBlockEntity(BlockPos.ZERO, block.defaultBlockState());
    }

    public static BlockEntityWithoutLevelRenderer create(Block block) {
        Minecraft minecraft = Minecraft.getInstance();
        return new ModBlockEntityWithoutLevelRenderer(block, minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack p_108830_, ItemTransforms.TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_) {
        this.blockEntityRenderDispatcher.renderItem(this.blockEntity, p_108832_, p_108833_, p_108834_, p_108835_);
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_172555_) {

    }

    public static IItemRenderProperties createItemRenderProperties(BlockItem blockItem) {
        return new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ModBlockEntityWithoutLevelRenderer.create(blockItem.getBlock());
            }
        };
    }
}
