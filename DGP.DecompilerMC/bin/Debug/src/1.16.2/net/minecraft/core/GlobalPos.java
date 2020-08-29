/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.core;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class GlobalPos {
    public static final Codec<GlobalPos> CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalPos::dimension), (App)BlockPos.CODEC.fieldOf("pos").forGetter(GlobalPos::pos)).apply((Applicative)instance, GlobalPos::of));
    private final ResourceKey<Level> dimension;
    private final BlockPos pos;

    private GlobalPos(ResourceKey<Level> resourceKey, BlockPos blockPos) {
        this.dimension = resourceKey;
        this.pos = blockPos;
    }

    public static GlobalPos of(ResourceKey<Level> resourceKey, BlockPos blockPos) {
        return new GlobalPos(resourceKey, blockPos);
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }

    public BlockPos pos() {
        return this.pos;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        GlobalPos globalPos = (GlobalPos)object;
        return Objects.equals(this.dimension, globalPos.dimension) && Objects.equals(this.pos, globalPos.pos);
    }

    public int hashCode() {
        return Objects.hash(this.dimension, this.pos);
    }

    public String toString() {
        return this.dimension.toString() + " " + this.pos;
    }
}

