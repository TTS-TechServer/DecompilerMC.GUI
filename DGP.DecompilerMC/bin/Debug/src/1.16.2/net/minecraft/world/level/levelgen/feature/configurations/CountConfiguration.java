/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CountConfiguration
implements DecoratorConfiguration,
FeatureConfiguration {
    public static final Codec<CountConfiguration> CODEC = UniformInt.codec(-10, 128, 128).fieldOf("count").xmap(CountConfiguration::new, CountConfiguration::count).codec();
    private final UniformInt count;

    public CountConfiguration(int n) {
        this.count = UniformInt.fixed(n);
    }

    public CountConfiguration(UniformInt uniformInt) {
        this.count = uniformInt;
    }

    public UniformInt count() {
        return this.count;
    }
}

