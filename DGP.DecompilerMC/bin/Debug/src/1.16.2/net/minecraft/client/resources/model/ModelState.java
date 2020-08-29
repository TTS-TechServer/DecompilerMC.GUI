/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.resources.model;

import com.mojang.math.Transformation;

public interface ModelState {
    default public Transformation getRotation() {
        return Transformation.identity();
    }

    default public boolean isUvLocked() {
        return false;
    }
}

