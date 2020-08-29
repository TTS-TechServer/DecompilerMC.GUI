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

public class DepthAverageConfigation
implements DecoratorConfiguration {
    public static final Codec<DepthAverageConfigation> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("baseline").forGetter(depthAverageConfigation -> depthAverageConfigation.baseline), (App)Codec.INT.fieldOf("spread").forGetter(depthAverageConfigation -> depthAverageConfigation.spread)).apply((Applicative)instance, DepthAverageConfigation::new));
    public final int baseline;
    public final int spread;

    public DepthAverageConfigation(int n, int n2) {
        this.baseline = n;
        this.spread = n2;
    }
}

