/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public class SkullModel
extends Model {
    protected final ModelPart head;

    public SkullModel() {
        this(0, 35, 64, 64);
    }

    public SkullModel(int n, int n2, int n3, int n4) {
        super(RenderType::entityTranslucent);
        this.texWidth = n3;
        this.texHeight = n4;
        this.head = new ModelPart(this, n, n2);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.0f);
        this.head.setPos(0.0f, 0.0f, 0.0f);
    }

    public void setupAnim(float f, float f2, float f3) {
        this.head.yRot = f2 * ((float)Math.PI / 180);
        this.head.xRot = f3 * ((float)Math.PI / 180);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int n, int n2, float f, float f2, float f3, float f4) {
        this.head.render(poseStack, vertexConsumer, n, n2, f, f2, f3, f4);
    }
}

