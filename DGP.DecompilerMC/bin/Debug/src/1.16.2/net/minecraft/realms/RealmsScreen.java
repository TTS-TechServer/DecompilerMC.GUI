/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.realms;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.TickableWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.realms.RealmsLabel;

public abstract class RealmsScreen
extends Screen {
    public RealmsScreen() {
        super(NarratorChatListener.NO_TITLE);
    }

    protected static int row(int n) {
        return 40 + n * 13;
    }

    @Override
    public void tick() {
        for (AbstractWidget abstractWidget : this.buttons) {
            if (!(abstractWidget instanceof TickableWidget)) continue;
            ((TickableWidget)((Object)abstractWidget)).tick();
        }
    }

    public void narrateLabels() {
        List<String> list = this.children.stream().filter(RealmsLabel.class::isInstance).map(RealmsLabel.class::cast).map(RealmsLabel::getText).collect(Collectors.toList());
        NarrationHelper.now(list);
    }
}

