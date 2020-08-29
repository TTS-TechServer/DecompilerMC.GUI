/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.level;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public class DataPackConfig {
    public static final DataPackConfig DEFAULT = new DataPackConfig((List<String>)ImmutableList.of((Object)"vanilla"), (List<String>)ImmutableList.of());
    public static final Codec<DataPackConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.STRING.listOf().fieldOf("Enabled").forGetter(dataPackConfig -> dataPackConfig.enabled), (App)Codec.STRING.listOf().fieldOf("Disabled").forGetter(dataPackConfig -> dataPackConfig.disabled)).apply((Applicative)instance, DataPackConfig::new));
    private final List<String> enabled;
    private final List<String> disabled;

    public DataPackConfig(List<String> list, List<String> list2) {
        this.enabled = ImmutableList.copyOf(list);
        this.disabled = ImmutableList.copyOf(list2);
    }

    public List<String> getEnabled() {
        return this.enabled;
    }

    public List<String> getDisabled() {
        return this.disabled;
    }
}

