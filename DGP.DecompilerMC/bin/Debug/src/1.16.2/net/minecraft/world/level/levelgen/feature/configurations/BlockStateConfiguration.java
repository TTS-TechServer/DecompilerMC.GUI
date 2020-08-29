/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BlockStateConfiguration
implements FeatureConfiguration {
    public static final Codec<BlockStateConfiguration> CODEC = BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, blockStateConfiguration -> blockStateConfiguration.state).codec();
    public final BlockState state;

    public BlockStateConfiguration(BlockState blockState) {
        this.state = blockState;
    }
}

