/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ShipwreckConfiguration
implements FeatureConfiguration {
    public static final Codec<ShipwreckConfiguration> CODEC = Codec.BOOL.fieldOf("is_beached").orElse((Object)false).xmap(ShipwreckConfiguration::new, shipwreckConfiguration -> shipwreckConfiguration.isBeached).codec();
    public final boolean isBeached;

    public ShipwreckConfiguration(boolean bl) {
        this.isBeached = bl;
    }
}

