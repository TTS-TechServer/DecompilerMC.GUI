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
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;

public class OceanRuinConfiguration
implements FeatureConfiguration {
    public static final Codec<OceanRuinConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)OceanRuinFeature.Type.CODEC.fieldOf("biome_temp").forGetter(oceanRuinConfiguration -> oceanRuinConfiguration.biomeTemp), (App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("large_probability").forGetter(oceanRuinConfiguration -> Float.valueOf(oceanRuinConfiguration.largeProbability)), (App)Codec.floatRange((float)0.0f, (float)1.0f).fieldOf("cluster_probability").forGetter(oceanRuinConfiguration -> Float.valueOf(oceanRuinConfiguration.clusterProbability))).apply((Applicative)instance, OceanRuinConfiguration::new));
    public final OceanRuinFeature.Type biomeTemp;
    public final float largeProbability;
    public final float clusterProbability;

    public OceanRuinConfiguration(OceanRuinFeature.Type type, float f, float f2) {
        this.biomeTemp = type;
        this.largeProbability = f;
        this.clusterProbability = f2;
    }
}

