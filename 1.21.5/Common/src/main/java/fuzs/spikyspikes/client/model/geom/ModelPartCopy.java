package fuzs.spikyspikes.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Map;

/**
 * Just a copy of {@link ModelPart} to bypass OptiFine/Sodium screwing with the vanilla class.
 * <p>
 * Thanks to tr9zw and their Skin Layers 3D mod for the idea!
 */
@Deprecated
public final class ModelPartCopy {
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean visible = true;
    private final List<ModelPart.Cube> cubes;
    private final Map<String, ModelPartCopy> children;

    public ModelPartCopy(List<ModelPart.Cube> cubes, Map<String, ModelPartCopy> children) {
        this.cubes = cubes;
        this.children = children;
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.render(poseStack, buffer, packedLight, packedOverlay, -1);
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (this.visible) {
            if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
                poseStack.pushPose();
                this.translateAndRotate(poseStack);
                this.compile(poseStack.last(), buffer, packedLight, packedOverlay, color);

                for (ModelPartCopy modelPart : this.children.values()) {
                    modelPart.render(poseStack, buffer, packedLight, packedOverlay, color);
                }

                poseStack.popPose();
            }
        }
    }

    public void translateAndRotate(PoseStack poseStack) {
        poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
        }
    }

    private void compile(PoseStack.Pose pose, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        for (ModelPart.Cube cube : this.cubes) {
            cube.compile(pose, buffer, packedLight, packedOverlay, color);
        }
    }
}