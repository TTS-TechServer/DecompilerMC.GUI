/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.RenderType;

public class ChunkBufferBuilderPack {
    private final Map<RenderType, BufferBuilder> builders = RenderType.chunkBufferLayers().stream().collect(Collectors.toMap(renderType -> renderType, renderType -> new BufferBuilder(renderType.bufferSize())));

    public BufferBuilder builder(RenderType renderType) {
        return this.builders.get(renderType);
    }

    public void clearAll() {
        this.builders.values().forEach(BufferBuilder::clear);
    }

    public void discardAll() {
        this.builders.values().forEach(BufferBuilder::discard);
    }
}

