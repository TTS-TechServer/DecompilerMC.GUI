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
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class RangeDecoratorConfiguration
implements DecoratorConfiguration {
    public static final Codec<RangeDecoratorConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("bottom_offset").orElse((Object)0).forGetter(rangeDecoratorConfiguration -> rangeDecoratorConfiguration.bottomOffset), (App)Codec.INT.fieldOf("top_offset").orElse((Object)0).forGetter(rangeDecoratorConfiguration -> rangeDecoratorConfiguration.topOffset), (App)Codec.INT.fieldOf("maximum").orElse((Object)0).forGetter(rangeDecoratorConfiguration -> rangeDecoratorConfiguration.maximum)).apply((Applicative)instance, RangeDecoratorConfiguration::new));
    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;

    public RangeDecoratorConfiguration(int n, int n2, int n3) {
        this.bottomOffset = n;
        this.topOffset = n2;
        this.maximum = n3;
    }
}

