/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class MobAppearanceParticle
extends Particle {
    private final Model model = new GuardianModel();
    private final RenderType renderType = RenderType.entityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_LOCATION);

    private MobAppearanceParticle(ClientLevel clientLevel, double d, double d2, double d3) {
        super(clientLevel, d, d2, d3);
        this.gravity = 0.0f;
        this.lifetime = 30;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
        float f2 = ((float)this.age + f) / (float)this.lifetime;
        float f3 = 0.05f + 0.5f * Mth.sin(f2 * (float)Math.PI);
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Vector3f.XP.rotationDegrees(150.0f * f2 - 60.0f));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0.0, -1.101f, 1.5);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer2 = bufferSource.getBuffer(this.renderType);
        this.model.renderToBuffer(poseStack, vertexConsumer2, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, f3);
        bufferSource.endBatch();
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new MobAppearanceParticle(clientLevel, d, d2, d3);
        }
    }
}

