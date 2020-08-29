/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  javax.annotation.Nullable
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SpikeConfiguration
implements FeatureConfiguration {
    public static final Codec<SpikeConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.BOOL.fieldOf("crystal_invulnerable").orElse((Object)false).forGetter(spikeConfiguration -> spikeConfiguration.crystalInvulnerable), (App)SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(spikeConfiguration -> spikeConfiguration.spikes), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(spikeConfiguration -> Optional.ofNullable(spikeConfiguration.crystalBeamTarget))).apply((Applicative)instance, SpikeConfiguration::new));
    private final boolean crystalInvulnerable;
    private final List<SpikeFeature.EndSpike> spikes;
    @Nullable
    private final BlockPos crystalBeamTarget;

    public SpikeConfiguration(boolean bl, List<SpikeFeature.EndSpike> list, @Nullable BlockPos blockPos) {
        this(bl, list, Optional.ofNullable(blockPos));
    }

    private SpikeConfiguration(boolean bl, List<SpikeFeature.EndSpike> list, Optional<BlockPos> optional) {
        this.crystalInvulnerable = bl;
        this.spikes = list;
        this.crystalBeamTarget = optional.orElse(null);
    }

    public boolean isCrystalInvulnerable() {
        return this.crystalInvulnerable;
    }

    public List<SpikeFeature.EndSpike> getSpikes() {
        return this.spikes;
    }

    @Nullable
    public BlockPos getCrystalBeamTarget() {
        return this.crystalBeamTarget;
    }
}

