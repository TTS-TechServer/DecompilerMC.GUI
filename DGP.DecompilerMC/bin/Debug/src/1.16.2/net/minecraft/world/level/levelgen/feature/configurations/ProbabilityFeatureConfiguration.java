/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ProbabilityFeatureConfiguration
implements CarverConfiguration,
FeatureConfiguration {
    public static final Codec<ProbabilityFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("probability").forGetter(probabilityFeatureConfiguration -> Float.valueOf(probabilityFeatureConfiguration.probability))).apply((Applicative)instance, ProbabilityFeatureConfiguration::new));
    public final float probability;

    public ProbabilityFeatureConfiguration(float f) {
        this.probability = f;
    }
}

