/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;
import net.minecraft.util.Unit;

public interface ReloadInstance {
    public CompletableFuture<Unit> done();

    public float getActualProgress();

    public boolean isApplying();

    public boolean isDone();

    public void checkExceptions();
}

