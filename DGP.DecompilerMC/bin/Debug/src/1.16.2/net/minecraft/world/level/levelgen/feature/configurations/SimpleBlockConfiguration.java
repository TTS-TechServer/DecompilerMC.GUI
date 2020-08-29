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
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SimpleBlockConfiguration
implements FeatureConfiguration {
    public static final Codec<SimpleBlockConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("to_place").forGetter(simpleBlockConfiguration -> simpleBlockConfiguration.toPlace), (App)BlockState.CODEC.listOf().fieldOf("place_on").forGetter(simpleBlockConfiguration -> simpleBlockConfiguration.placeOn), (App)BlockState.CODEC.listOf().fieldOf("place_in").forGetter(simpleBlockConfiguration -> simpleBlockConfiguration.placeIn), (App)BlockState.CODEC.listOf().fieldOf("place_under").forGetter(simpleBlockConfiguration -> simpleBlockConfiguration.placeUnder)).apply((Applicative)instance, SimpleBlockConfiguration::new));
    public final BlockState toPlace;
    public final List<BlockState> placeOn;
    public final List<BlockState> placeIn;
    public final List<BlockState> placeUnder;

    public SimpleBlockConfiguration(BlockState blockState, List<BlockState> list, List<BlockState> list2, List<BlockState> list3) {
        this.toPlace = blockState;
        this.placeOn = list;
        this.placeIn = list2;
        this.placeUnder = list3;
    }
}

