/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NoiseSamplingSettings {
    private static final Codec<Double> SCALE_RANGE = Codec.doubleRange((double)0.001, (double)1000.0);
    public static final Codec<NoiseSamplingSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)SCALE_RANGE.fieldOf("xz_scale").forGetter(NoiseSamplingSettings::xzScale), (App)SCALE_RANGE.fieldOf("y_scale").forGetter(NoiseSamplingSettings::yScale), (App)SCALE_RANGE.fieldOf("xz_factor").forGetter(NoiseSamplingSettings::xzFactor), (App)SCALE_RANGE.fieldOf("y_factor").forGetter(NoiseSamplingSettings::yFactor)).apply((Applicative)instance, NoiseSamplingSettings::new));
    private final double xzScale;
    private final double yScale;
    private final double xzFactor;
    private final double yFactor;

    public NoiseSamplingSettings(double d, double d2, double d3, double d4) {
        this.xzScale = d;
        this.yScale = d2;
        this.xzFactor = d3;
        this.yFactor = d4;
    }

    public double xzScale() {
        return this.xzScale;
    }

    public double yScale() {
        return this.yScale;
    }

    public double xzFactor() {
        return this.xzFactor;
    }

    public double yFactor() {
        return this.yFactor;
    }
}

