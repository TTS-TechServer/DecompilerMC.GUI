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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;

public class DustParticle
extends TextureSheetParticle {
    private final SpriteSet sprites;

    private DustParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, DustParticleOptions dustParticleOptions, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.sprites = spriteSet;
        this.xd *= (double)0.1f;
        this.yd *= (double)0.1f;
        this.zd *= (double)0.1f;
        float f = (float)Math.random() * 0.4f + 0.6f;
        this.rCol = ((float)(Math.random() * (double)0.2f) + 0.8f) * dustParticleOptions.getR() * f;
        this.gCol = ((float)(Math.random() * (double)0.2f) + 0.8f) * dustParticleOptions.getG() * f;
        this.bCol = ((float)(Math.random() * (double)0.2f) + 0.8f) * dustParticleOptions.getB() * f;
        this.quadSize *= 0.75f * dustParticleOptions.getScale();
        int n = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.lifetime = (int)Math.max((float)n * dustParticleOptions.getScale(), 1.0f);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float f) {
        return this.quadSize * Mth.clamp(((float)this.age + f) / (float)this.lifetime * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.setSpriteFromAge(this.sprites);
        this.move(this.xd, this.yd, this.zd);
        if (this.y == this.yo) {
            this.xd *= 1.1;
            this.zd *= 1.1;
        }
        this.xd *= (double)0.96f;
        this.yd *= (double)0.96f;
        this.zd *= (double)0.96f;
        if (this.onGround) {
            this.xd *= (double)0.7f;
            this.zd *= (double)0.7f;
        }
    }

    public static class Provider
    implements ParticleProvider<DustParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(DustParticleOptions dustParticleOptions, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new DustParticle(clientLevel, d, d2, d3, d4, d5, d6, dustParticleOptions, this.sprites);
        }
    }
}

