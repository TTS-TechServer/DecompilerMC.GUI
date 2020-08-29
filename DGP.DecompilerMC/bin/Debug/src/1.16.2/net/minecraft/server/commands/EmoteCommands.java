/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;

public class EmoteCommands {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)Commands.literal("me").then(Commands.argument("action", StringArgumentType.greedyString()).executes(commandContext -> {
            TranslatableComponent translatableComponent = new TranslatableComponent("chat.type.emote", ((CommandSourceStack)commandContext.getSource()).getDisplayName(), StringArgumentType.getString((CommandContext)commandContext, (String)"action"));
            Entity entity = ((CommandSourceStack)commandContext.getSource()).getEntity();
            if (entity != null) {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(translatableComponent, ChatType.CHAT, entity.getUUID());
            } else {
                ((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().broadcastMessage(translatableComponent, ChatType.SYSTEM, Util.NIL_UUID);
            }
            return 1;
        })));
    }
}

