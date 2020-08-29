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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;

public class WaterDropParticle
extends TextureSheetParticle {
    protected WaterDropParticle(ClientLevel clientLevel, double d, double d2, double d3) {
        super(clientLevel, d, d2, d3, 0.0, 0.0, 0.0);
        this.xd *= (double)0.3f;
        this.yd = Math.random() * (double)0.2f + (double)0.1f;
        this.zd *= (double)0.3f;
        this.setSize(0.01f, 0.01f);
        this.gravity = 0.06f;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        BlockPos blockPos;
        double d;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        this.yd -= (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= (double)0.98f;
        this.yd *= (double)0.98f;
        this.zd *= (double)0.98f;
        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.remove();
            }
            this.xd *= (double)0.7f;
            this.zd *= (double)0.7f;
        }
        if ((d = Math.max(this.level.getBlockState(blockPos = new BlockPos(this.x, this.y, this.z)).getCollisionShape(this.level, blockPos).max(Direction.Axis.Y, this.x - (double)blockPos.getX(), this.z - (double)blockPos.getZ()), (double)this.level.getFluidState(blockPos).getHeight(this.level, blockPos))) > 0.0 && this.y < (double)blockPos.getY() + d) {
            this.remove();
        }
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            WaterDropParticle waterDropParticle = new WaterDropParticle(clientLevel, d, d2, d3);
            waterDropParticle.pickSprite(this.sprite);
            return waterDropParticle;
        }
    }
}

