/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.level.newbiome.layer.traits;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.Context;

public interface AreaTransformer0 {
    default public <R extends Area> AreaFactory<R> run(BigContext<R> bigContext) {
        return () -> bigContext.createResult((n, n2) -> {
            bigContext.initRandom(n, n2);
            return this.applyPixel(bigContext, n, n2);
        });
    }

    public int applyPixel(Context var1, int var2, int var3);
}

