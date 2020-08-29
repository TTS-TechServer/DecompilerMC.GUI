/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.blaze3d.shaders;

import com.mojang.blaze3d.shaders.Program;

public interface Effect {
    public int getId();

    public void markDirty();

    public Program getVertexProgram();

    public Program getFragmentProgram();
}

