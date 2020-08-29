/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class NoiseCountFactorDecoratorConfiguration
implements DecoratorConfiguration {
    public static final Codec<NoiseCountFactorDecoratorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("noise_to_count_ratio").forGetter(noiseCountFactorDecoratorConfiguration -> noiseCountFactorDecoratorConfiguration.noiseToCountRatio), (App)Codec.DOUBLE.fieldOf("noise_factor").forGetter(noiseCountFactorDecoratorConfiguration -> noiseCountFactorDecoratorConfiguration.noiseFactor), (App)Codec.DOUBLE.fieldOf("noise_offset").orElse((Object)0.0).forGetter(noiseCountFactorDecoratorConfiguration -> noiseCountFactorDecoratorConfiguration.noiseOffset)).apply((Applicative)instance, NoiseCountFactorDecoratorConfiguration::new));
    public final int noiseToCountRatio;
    public final double noiseFactor;
    public final double noiseOffset;

    public NoiseCountFactorDecoratorConfiguration(int n, double d, double d2) {
        this.noiseToCountRatio = n;
        this.noiseFactor = d;
        this.noiseOffset = d2;
    }
}

