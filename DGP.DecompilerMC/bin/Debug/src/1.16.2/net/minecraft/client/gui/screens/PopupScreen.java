/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 */
package net.minecraft.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class PopupScreen
extends Screen {
    private final FormattedText message;
    private final ImmutableList<ButtonOption> buttonOptions;
    private MultiLineLabel messageLines = MultiLineLabel.EMPTY;
    private int contentTop;
    private int buttonWidth;

    protected PopupScreen(Component component, List<FormattedText> list, ImmutableList<ButtonOption> immutableList) {
        super(component);
        this.message = FormattedText.composite(list);
        this.buttonOptions = immutableList;
    }

    @Override
    public String getNarrationMessage() {
        return super.getNarrationMessage() + ". " + this.message.getString();
    }

    @Override
    public void init(Minecraft minecraft, int n, int n2) {
        super.init(minecraft, n, n2);
        for (ButtonOption buttonOption : this.buttonOptions) {
            this.buttonWidth = Math.max(this.buttonWidth, 20 + this.font.width(buttonOption.message) + 20);
        }
        int n3 = 5 + this.buttonWidth + 5;
        int n4 = n3 * this.buttonOptions.size();
        this.messageLines = MultiLineLabel.create(this.font, this.message, n4);
        int n5 = this.messageLines.getLineCount();
        this.font.getClass();
        int n6 = n5 * 9;
        this.contentTop = (int)((double)n2 / 2.0 - (double)n6 / 2.0);
        this.font.getClass();
        int n7 = this.contentTop + n6 + 9 * 2;
        int n8 = (int)((double)n / 2.0 - (double)n4 / 2.0);
        for (ButtonOption buttonOption : this.buttonOptions) {
            this.addButton(new Button(n8, n7, this.buttonWidth, 20, buttonOption.message, buttonOption.onPress));
            n8 += n3;
        }
    }

    @Override
    public void render(PoseStack poseStack, int n, int n2, float f) {
        this.renderDirtBackground(0);
        int n3 = this.width / 2;
        this.font.getClass();
        PopupScreen.drawCenteredString(poseStack, this.font, this.title, n3, this.contentTop - 9 * 2, -1);
        this.messageLines.renderCentered(poseStack, this.width / 2, this.contentTop);
        super.render(poseStack, n, n2, f);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public static final class ButtonOption {
        private final Component message;
        private final Button.OnPress onPress;

        public ButtonOption(Component component, Button.OnPress onPress) {
            this.message = component;
            this.onPress = onPress;
        }
    }
}

