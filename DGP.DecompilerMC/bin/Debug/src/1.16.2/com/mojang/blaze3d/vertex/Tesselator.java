/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;

public class Tesselator {
    private final BufferBuilder builder;
    private static final Tesselator INSTANCE = new Tesselator();

    public static Tesselator getInstance() {
        RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
        return INSTANCE;
    }

    public Tesselator(int n) {
        this.builder = new BufferBuilder(n);
    }

    public Tesselator() {
        this(0x200000);
    }

    public void end() {
        this.builder.end();
        BufferUploader.end(this.builder);
    }

    public BufferBuilder getBuilder() {
        return this.builder;
    }
}

