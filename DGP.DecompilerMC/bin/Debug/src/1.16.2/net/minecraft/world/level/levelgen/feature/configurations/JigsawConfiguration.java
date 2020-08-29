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
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class JigsawConfiguration
implements FeatureConfiguration {
    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool), (App)Codec.intRange((int)0, (int)7).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)).apply((Applicative)instance, JigsawConfiguration::new));
    private final Supplier<StructureTemplatePool> startPool;
    private final int maxDepth;

    public JigsawConfiguration(Supplier<StructureTemplatePool> supplier, int n) {
        this.startPool = supplier;
        this.maxDepth = n;
    }

    public int maxDepth() {
        return this.maxDepth;
    }

    public Supplier<StructureTemplatePool> startPool() {
        return this.startPool;
    }
}

