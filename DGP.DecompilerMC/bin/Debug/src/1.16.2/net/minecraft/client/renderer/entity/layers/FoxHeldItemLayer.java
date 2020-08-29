/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;

public class FoxHeldItemLayer
extends RenderLayer<Fox, FoxModel<Fox>> {
    public FoxHeldItemLayer(RenderLayerParent<Fox, FoxModel<Fox>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int n, Fox fox, float f, float f2, float f3, float f4, float f5, float f6) {
        float f7;
        boolean bl = fox.isSleeping();
        boolean bl2 = fox.isBaby();
        poseStack.pushPose();
        if (bl2) {
            f7 = 0.75f;
            poseStack.scale(0.75f, 0.75f, 0.75f);
            poseStack.translate(0.0, 0.5, 0.209375f);
        }
        poseStack.translate(((FoxModel)this.getParentModel()).head.x / 16.0f, ((FoxModel)this.getParentModel()).head.y / 16.0f, ((FoxModel)this.getParentModel()).head.z / 16.0f);
        f7 = fox.getHeadRollAngle(f3);
        poseStack.mulPose(Vector3f.ZP.rotation(f7));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f5));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(f6));
        if (fox.isBaby()) {
            if (bl) {
                poseStack.translate(0.4f, 0.26f, 0.15f);
            } else {
                poseStack.translate(0.06f, 0.26f, -0.5);
            }
        } else if (bl) {
            poseStack.translate(0.46f, 0.26f, 0.22f);
        } else {
            poseStack.translate(0.06f, 0.27f, -0.5);
        }
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        if (bl) {
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
        ItemStack itemStack = fox.getItemBySlot(EquipmentSlot.MAINHAND);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(fox, itemStack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, n);
        poseStack.popPose();
    }
}

