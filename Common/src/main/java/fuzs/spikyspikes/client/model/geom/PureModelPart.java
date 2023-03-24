package fuzs.spikyspikes.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;

/**
 * just a copy of {@link ModelPart} to bypass OptiFine/Sodium screwing with the vanilla class
 *
 * <p>thanks to tr9zw and their Skin Layers 3D mod for the idea
 */
public final class PureModelPart {
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean visible = true;
    private final List<ModelPart.Cube> cubes;
    private final Map<String, PureModelPart> children;

    public PureModelPart(List<ModelPart.Cube> p_171306_, Map<String, PureModelPart> p_171307_) {
        this.cubes = p_171306_;
        this.children = p_171307_;
    }

    public void render(PoseStack p_104302_, VertexConsumer p_104303_, int p_104304_, int p_104305_) {
        this.render(p_104302_, p_104303_, p_104304_, p_104305_, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack p_104307_, VertexConsumer p_104308_, int p_104309_, int p_104310_, float p_104311_, float p_104312_, float p_104313_, float p_104314_) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                p_104307_.pushPose();
                this.translateAndRotate(p_104307_);
                this.compile(p_104307_.last(), p_104308_, p_104309_, p_104310_, p_104311_, p_104312_, p_104313_, p_104314_);

                for (PureModelPart modelpart : this.children.values()) {
                    modelpart.render(p_104307_, p_104308_, p_104309_, p_104310_, p_104311_, p_104312_, p_104313_, p_104314_);
                }

                p_104307_.popPose();
            }
        }
    }

    public void translateAndRotate(PoseStack p_104300_) {
        p_104300_.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.zRot != 0.0F) {
            p_104300_.mulPose(Axis.ZP.rotation(this.zRot));
        }

        if (this.yRot != 0.0F) {
            p_104300_.mulPose(Axis.YP.rotation(this.yRot));
        }

        if (this.xRot != 0.0F) {
            p_104300_.mulPose(Axis.XP.rotation(this.xRot));
        }

    }

    private void compile(PoseStack.Pose p_104291_, VertexConsumer p_104292_, int p_104293_, int p_104294_, float p_104295_, float p_104296_, float p_104297_, float p_104298_) {
        for (ModelPart.Cube modelpart$cube : this.cubes) {
            modelpart$cube.compile(p_104291_, p_104292_, p_104293_, p_104294_, p_104295_, p_104296_, p_104297_, p_104298_);
        }

    }
}