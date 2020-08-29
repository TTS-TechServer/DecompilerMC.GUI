/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PlayerCloudParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;

    private PlayerCloudParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, 0.0, 0.0, 0.0);
        float f;
        this.sprites = spriteSet;
        float f2 = 2.5f;
        this.xd *= (double)0.1f;
        this.yd *= (double)0.1f;
        this.zd *= (double)0.1f;
        this.xd += d4;
        this.yd += d5;
        this.zd += d6;
        this.rCol = f = 1.0f - (float)(Math.random() * (double)0.3f);
        this.gCol = f;
        this.bCol = f;
        this.quadSize *= 1.875f;
        int n = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)n * 2.5f, 1.0f);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float f) {
        return this.quadSize * Mth.clamp(((float)this.age + f) / (float)this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        double d;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
        this.move(this.xd, this.yd, this.zd);
        this.xd *= (double)0.96f;
        this.yd *= (double)0.96f;
        this.zd *= (double)0.96f;
        Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
        if (player != null && this.y > (d = player.getY())) {
            this.y += (d - this.y) * 0.2;
            this.yd += (player.getDeltaMovement().y - this.yd) * 0.2;
            this.setPos(this.x, this.y, this.z);
        }
        if (this.onGround) {
            this.xd *= (double)0.7f;
            this.zd *= (double)0.7f;
        }
    }

    public static class SneezeProvider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public SneezeProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            PlayerCloudParticle playerCloudParticle = new PlayerCloudParticle(clientLevel, d, d2, d3, d4, d5, d6, this.sprites);
            playerCloudParticle.setColor(200.0f, 50.0f, 120.0f);
            playerCloudParticle.setAlpha(0.4f);
            return playerCloudParticle;
        }
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new PlayerCloudParticle(clientLevel, d, d2, d3, d4, d5, d6, this.sprites);
        }
    }
}

