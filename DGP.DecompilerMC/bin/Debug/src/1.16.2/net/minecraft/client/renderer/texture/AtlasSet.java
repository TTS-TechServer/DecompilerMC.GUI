/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class AtlasSet
implements AutoCloseable {
    private final Map<ResourceLocation, TextureAtlas> atlases;

    public AtlasSet(Collection<TextureAtlas> collection) {
        this.atlases = collection.stream().collect(Collectors.toMap(TextureAtlas::location, Function.identity()));
    }

    public TextureAtlas getAtlas(ResourceLocation resourceLocation) {
        return this.atlases.get(resourceLocation);
    }

    public TextureAtlasSprite getSprite(Material material) {
        return this.atlases.get(material.atlasLocation()).getSprite(material.texture());
    }

    @Override
    public void close() {
        this.atlases.values().forEach(TextureAtlas::clearTextureData);
        this.atlases.clear();
    }
}

