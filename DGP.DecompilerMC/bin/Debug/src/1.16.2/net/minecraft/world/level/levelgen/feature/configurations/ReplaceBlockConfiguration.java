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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ReplaceBlockConfiguration
implements FeatureConfiguration {
    public static final Codec<ReplaceBlockConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("target").forGetter(replaceBlockConfiguration -> replaceBlockConfiguration.target), (App)BlockState.CODEC.fieldOf("state").forGetter(replaceBlockConfiguration -> replaceBlockConfiguration.state)).apply((Applicative)instance, ReplaceBlockConfiguration::new));
    public final BlockState target;
    public final BlockState state;

    public ReplaceBlockConfiguration(BlockState blockState, BlockState blockState2) {
        this.target = blockState;
        this.state = blockState2;
    }
}

