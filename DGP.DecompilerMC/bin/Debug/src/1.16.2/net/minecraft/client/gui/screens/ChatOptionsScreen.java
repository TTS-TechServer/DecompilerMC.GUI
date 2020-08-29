/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui.screens;

import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.TranslatableComponent;

public class ChatOptionsScreen
extends SimpleOptionsSubScreen {
    private static final Option[] CHAT_OPTIONS = new Option[]{Option.CHAT_VISIBILITY, Option.CHAT_COLOR, Option.CHAT_LINKS, Option.CHAT_LINKS_PROMPT, Option.CHAT_OPACITY, Option.TEXT_BACKGROUND_OPACITY, Option.CHAT_SCALE, Option.CHAT_LINE_SPACING, Option.CHAT_DELAY, Option.CHAT_WIDTH, Option.CHAT_HEIGHT_FOCUSED, Option.CHAT_HEIGHT_UNFOCUSED, Option.NARRATOR, Option.AUTO_SUGGESTIONS, Option.REDUCED_DEBUG_INFO};

    public ChatOptionsScreen(Screen screen, Options options) {
        super(screen, options, new TranslatableComponent("options.chat.title"), CHAT_OPTIONS);
    }
}

