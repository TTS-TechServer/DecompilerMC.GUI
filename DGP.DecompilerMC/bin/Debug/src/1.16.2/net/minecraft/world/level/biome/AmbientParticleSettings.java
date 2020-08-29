/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

public class AmbientParticleSettings {
    public static final Codec<AmbientParticleSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ParticleTypes.CODEC.fieldOf("options").forGetter(ambientParticleSettings -> ambientParticleSettings.options), (App)Codec.FLOAT.fieldOf("probability").forGetter(ambientParticleSettings -> Float.valueOf(ambientParticleSettings.probability))).apply((Applicative)instance, AmbientParticleSettings::new));
    private final ParticleOptions options;
    private final float probability;

    public AmbientParticleSettings(ParticleOptions particleOptions, float f) {
        this.options = particleOptions;
        this.probability = f;
    }

    public ParticleOptions getOptions() {
        return this.options;
    }

    public boolean canSpawn(Random random) {
        return random.nextFloat() <= this.probability;
    }
}

