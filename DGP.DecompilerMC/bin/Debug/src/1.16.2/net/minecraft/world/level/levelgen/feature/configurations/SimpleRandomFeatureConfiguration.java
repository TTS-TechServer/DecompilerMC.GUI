/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SimpleRandomFeatureConfiguration
implements FeatureConfiguration {
    public static final Codec<SimpleRandomFeatureConfiguration> CODEC = ConfiguredFeature.LIST_CODEC.fieldOf("features").xmap(SimpleRandomFeatureConfiguration::new, simpleRandomFeatureConfiguration -> simpleRandomFeatureConfiguration.features).codec();
    public final List<Supplier<ConfiguredFeature<?, ?>>> features;

    public SimpleRandomFeatureConfiguration(List<Supplier<ConfiguredFeature<?, ?>>> list) {
        this.features = list;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return this.features.stream().flatMap(supplier -> ((ConfiguredFeature)supplier.get()).getFeatures());
    }
}

