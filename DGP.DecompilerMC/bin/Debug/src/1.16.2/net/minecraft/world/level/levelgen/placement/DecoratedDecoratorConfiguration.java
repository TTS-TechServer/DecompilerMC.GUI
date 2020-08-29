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
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;

public class DecoratedDecoratorConfiguration
implements DecoratorConfiguration {
    public static final Codec<DecoratedDecoratorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredDecorator.CODEC.fieldOf("outer").forGetter(DecoratedDecoratorConfiguration::outer), (App)ConfiguredDecorator.CODEC.fieldOf("inner").forGetter(DecoratedDecoratorConfiguration::inner)).apply((Applicative)instance, DecoratedDecoratorConfiguration::new));
    private final ConfiguredDecorator<?> outer;
    private final ConfiguredDecorator<?> inner;

    public DecoratedDecoratorConfiguration(ConfiguredDecorator<?> configuredDecorator, ConfiguredDecorator<?> configuredDecorator2) {
        this.outer = configuredDecorator;
        this.inner = configuredDecorator2;
    }

    public ConfiguredDecorator<?> outer() {
        return this.outer;
    }

    public ConfiguredDecorator<?> inner() {
        return this.inner;
    }
}

