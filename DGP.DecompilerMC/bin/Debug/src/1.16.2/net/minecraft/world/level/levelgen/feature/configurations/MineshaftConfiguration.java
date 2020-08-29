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
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class MineshaftConfiguration
implements FeatureConfiguration {
    public static final Codec<MineshaftConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("probability").forGetter(mineshaftConfiguration -> Float.valueOf(mineshaftConfiguration.probability)), (App)MineshaftFeature.Type.CODEC.fieldOf("type").forGetter(mineshaftConfiguration -> mineshaftConfiguration.type)).apply((Applicative)instance, MineshaftConfiguration::new));
    public final float probability;
    public final MineshaftFeature.Type type;

    public MineshaftConfiguration(float f, MineshaftFeature.Type type) {
        this.probability = f;
        this.type = type;
    }
}

