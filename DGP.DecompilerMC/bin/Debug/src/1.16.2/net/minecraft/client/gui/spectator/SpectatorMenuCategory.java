/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuCategory {
    public List<SpectatorMenuItem> getItems();

    public Component getPrompt();
}

