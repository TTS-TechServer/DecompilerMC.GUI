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
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;

public class DecoratedFeatureConfiguration
implements FeatureConfiguration {
    public static final Codec<DecoratedFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(decoratedFeatureConfiguration -> decoratedFeatureConfiguration.feature), (App)ConfiguredDecorator.CODEC.fieldOf("decorator").forGetter(decoratedFeatureConfiguration -> decoratedFeatureConfiguration.decorator)).apply((Applicative)instance, DecoratedFeatureConfiguration::new));
    public final Supplier<ConfiguredFeature<?, ?>> feature;
    public final ConfiguredDecorator<?> decorator;

    public DecoratedFeatureConfiguration(Supplier<ConfiguredFeature<?, ?>> supplier, ConfiguredDecorator<?> configuredDecorator) {
        this.feature = supplier;
        this.decorator = configuredDecorator;
    }

    public String toString() {
        return String.format("< %s [%s | %s] >", this.getClass().getSimpleName(), Registry.FEATURE.getKey((Feature<?>)this.feature.get().feature()), this.decorator);
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return this.feature.get().getFeatures();
    }
}

