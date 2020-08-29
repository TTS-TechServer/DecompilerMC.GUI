/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class ChanceDecoratorConfiguration
implements DecoratorConfiguration {
    public static final Codec<ChanceDecoratorConfiguration> CODEC = Codec.INT.fieldOf("chance").xmap(ChanceDecoratorConfiguration::new, chanceDecoratorConfiguration -> chanceDecoratorConfiguration.chance).codec();
    public final int chance;

    public ChanceDecoratorConfiguration(int n) {
        this.chance = n;
    }
}

