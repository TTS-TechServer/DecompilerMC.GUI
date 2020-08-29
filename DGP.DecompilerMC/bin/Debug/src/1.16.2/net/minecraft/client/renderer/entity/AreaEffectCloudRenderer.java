/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;

public class AreaEffectCloudRenderer
extends EntityRenderer<AreaEffectCloud> {
    public AreaEffectCloudRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public ResourceLocation getTextureLocation(AreaEffectCloud areaEffectCloud) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

