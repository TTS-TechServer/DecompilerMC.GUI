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
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ReplaceSphereConfiguration
implements FeatureConfiguration {
    public static final Codec<ReplaceSphereConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("target").forGetter(replaceSphereConfiguration -> replaceSphereConfiguration.targetState), (App)BlockState.CODEC.fieldOf("state").forGetter(replaceSphereConfiguration -> replaceSphereConfiguration.replaceState), (App)UniformInt.CODEC.fieldOf("radius").forGetter(replaceSphereConfiguration -> replaceSphereConfiguration.radius)).apply((Applicative)instance, ReplaceSphereConfiguration::new));
    public final BlockState targetState;
    public final BlockState replaceState;
    private final UniformInt radius;

    public ReplaceSphereConfiguration(BlockState blockState, BlockState blockState2, UniformInt uniformInt) {
        this.targetState = blockState;
        this.replaceState = blockState2;
        this.radius = uniformInt;
    }

    public UniformInt radius() {
        return this.radius;
    }
}

