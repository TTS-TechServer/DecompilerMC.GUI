/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

public class StandardChatListener
implements ChatListener {
    private final Minecraft minecraft;

    public StandardChatListener(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void handle(ChatType chatType, Component component, UUID uUID) {
        if (this.minecraft.isBlocked(uUID)) {
            return;
        }
        if (chatType != ChatType.CHAT) {
            this.minecraft.gui.getChat().addMessage(component);
        } else {
            this.minecraft.gui.getChat().enqueueMessage(component);
        }
    }
}

