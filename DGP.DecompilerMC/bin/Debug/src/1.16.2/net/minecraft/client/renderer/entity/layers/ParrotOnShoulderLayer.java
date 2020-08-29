/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class ParrotOnShoulderLayer<T extends Player>
extends RenderLayer<T, PlayerModel<T>> {
    private final ParrotModel model = new ParrotModel();

    public ParrotOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int n, T t, float f, float f2, float f3, float f4, float f5, float f6) {
        this.render(poseStack, multiBufferSource, n, t, f, f2, f5, f6, true);
        this.render(poseStack, multiBufferSource, n, t, f, f2, f5, f6, false);
    }

    private void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int n, T t, float f, float f2, float f3, float f4, boolean bl) {
        CompoundTag compoundTag = bl ? ((Player)t).getShoulderEntityLeft() : ((Player)t).getShoulderEntityRight();
        EntityType.byString(compoundTag.getString("id")).filter(entityType -> entityType == EntityType.PARROT).ifPresent(entityType -> {
            poseStack.pushPose();
            poseStack.translate(bl ? (double)0.4f : (double)-0.4f, t.isCrouching() ? (double)-1.3f : -1.5, 0.0);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(ParrotRenderer.PARROT_LOCATIONS[compoundTag.getInt("Variant")]));
            this.model.renderOnShoulder(poseStack, vertexConsumer, n, OverlayTexture.NO_OVERLAY, f, f2, f3, f4, player.tickCount);
            poseStack.popPose();
        });
    }
}

