/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.RuinedPortalFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class RuinedPortalConfiguration
implements FeatureConfiguration {
    public static final Codec<RuinedPortalConfiguration> CODEC = RuinedPortalFeature.Type.CODEC.fieldOf("portal_type").xmap(RuinedPortalConfiguration::new, ruinedPortalConfiguration -> ruinedPortalConfiguration.portalType).codec();
    public final RuinedPortalFeature.Type portalType;

    public RuinedPortalConfiguration(RuinedPortalFeature.Type type) {
        this.portalType = type;
    }
}

