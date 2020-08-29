/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class StrayRenderer
extends SkeletonRenderer {
    private static final ResourceLocation STRAY_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/stray.png");

    public StrayRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        this.addLayer(new StrayClothingLayer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton abstractSkeleton) {
        return STRAY_SKELETON_LOCATION;
    }
}

