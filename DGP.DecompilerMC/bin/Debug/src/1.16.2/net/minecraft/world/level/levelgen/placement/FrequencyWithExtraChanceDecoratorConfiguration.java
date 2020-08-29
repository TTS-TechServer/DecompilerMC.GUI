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

public class FrequencyWithExtraChanceDecoratorConfiguration
implements DecoratorConfiguration {
    public static final Codec<FrequencyWithExtraChanceDecoratorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("count").forGetter(frequencyWithExtraChanceDecoratorConfiguration -> frequencyWithExtraChanceDecoratorConfiguration.count), (App)Codec.FLOAT.fieldOf("extra_chance").forGetter(frequencyWithExtraChanceDecoratorConfiguration -> Float.valueOf(frequencyWithExtraChanceDecoratorConfiguration.extraChance)), (App)Codec.INT.fieldOf("extra_count").forGetter(frequencyWithExtraChanceDecoratorConfiguration -> frequencyWithExtraChanceDecoratorConfiguration.extraCount)).apply((Applicative)instance, FrequencyWithExtraChanceDecoratorConfiguration::new));
    public final int count;
    public final float extraChance;
    public final int extraCount;

    public FrequencyWithExtraChanceDecoratorConfiguration(int n, float f, int n2) {
        this.count = n;
        this.extraChance = f;
        this.extraCount = n2;
    }
}

