/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.CodModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cod;

public class CodRenderer
extends MobRenderer<Cod, CodModel<Cod>> {
    private static final ResourceLocation COD_LOCATION = new ResourceLocation("textures/entity/fish/cod.png");

    public CodRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CodModel(), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(Cod cod) {
        return COD_LOCATION;
    }

    @Override
    protected void setupRotations(Cod cod, PoseStack poseStack, float f, float f2, float f3) {
        super.setupRotations(cod, poseStack, f, f2, f3);
        float f4 = 4.3f * Mth.sin(0.6f * f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(f4));
        if (!cod.isInWater()) {
            poseStack.translate(0.1f, 0.1f, -0.1f);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
    }
}

