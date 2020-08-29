/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.PhantomModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Phantom;

public class PhantomRenderer
extends MobRenderer<Phantom, PhantomModel<Phantom>> {
    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");

    public PhantomRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new PhantomModel(), 0.75f);
        this.addLayer(new PhantomEyesLayer<Phantom>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Phantom phantom) {
        return PHANTOM_LOCATION;
    }

    @Override
    protected void scale(Phantom phantom, PoseStack poseStack, float f) {
        int n = phantom.getPhantomSize();
        float f2 = 1.0f + 0.15f * (float)n;
        poseStack.scale(f2, f2, f2);
        poseStack.translate(0.0, 1.3125, 0.1875);
    }

    @Override
    protected void setupRotations(Phantom phantom, PoseStack poseStack, float f, float f2, float f3) {
        super.setupRotations(phantom, poseStack, f, f2, f3);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(phantom.xRot));
    }
}

