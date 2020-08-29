/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;

public class DisconnectedScreen
extends Screen {
    private final Component reason;
    private MultiLineLabel message = MultiLineLabel.EMPTY;
    private final Screen parent;
    private int textHeight;

    public DisconnectedScreen(Screen screen, Component component, Component component2) {
        super(component);
        this.parent = screen;
        this.reason = component2;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        this.message = MultiLineLabel.create(this.font, (FormattedText)this.reason, this.width - 50);
        int n = this.message.getLineCount();
        this.font.getClass();
        this.textHeight = n * 9;
        int n2 = this.width / 2 - 100;
        int n3 = this.height / 2 + this.textHeight / 2;
        this.font.getClass();
        this.addButton(new Button(n2, Math.min(n3 + 9, this.height - 30), 200, 20, new TranslatableComponent("gui.toMenu"), button -> this.minecraft.setScreen(this.parent)));
    }

    @Override
    public void render(PoseStack poseStack, int n, int n2, float f) {
        this.renderBackground(poseStack);
        int n3 = this.width / 2;
        int n4 = this.height / 2 - this.textHeight / 2;
        this.font.getClass();
        DisconnectedScreen.drawCenteredString(poseStack, this.font, this.title, n3, n4 - 9 * 2, 0xAAAAAA);
        this.message.renderCentered(poseStack, this.width / 2, this.height / 2 - this.textHeight / 2);
        super.render(poseStack, n, n2, f);
    }
}

