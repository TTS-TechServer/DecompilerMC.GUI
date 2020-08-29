/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.texture;

import java.util.Collection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class StitcherException
extends RuntimeException {
    private final Collection<TextureAtlasSprite.Info> allSprites;

    public StitcherException(TextureAtlasSprite.Info info, Collection<TextureAtlasSprite.Info> collection) {
        super(String.format("Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", info.name(), info.width(), info.height()));
        this.allSprites = collection;
    }

    public Collection<TextureAtlasSprite.Info> getAllSprites() {
        return this.allSprites;
    }
}

