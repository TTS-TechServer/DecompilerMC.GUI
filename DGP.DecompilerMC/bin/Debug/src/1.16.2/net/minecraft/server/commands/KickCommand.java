/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class KickCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kick").requires(commandSourceStack -> commandSourceStack.hasPermission(3))).then(((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players()).executes(commandContext -> KickCommand.kickPlayers((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), new TranslatableComponent("multiplayer.disconnect.kicked")))).then(Commands.argument("reason", MessageArgument.message()).executes(commandContext -> KickCommand.kickPlayers((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), MessageArgument.getMessage((CommandContext<CommandSourceStack>)commandContext, "reason"))))));
    }

    private static int kickPlayers(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, Component component) {
        for (ServerPlayer serverPlayer : collection) {
            serverPlayer.connection.disconnect(component);
            commandSourceStack.sendSuccess(new TranslatableComponent("commands.kick.success", serverPlayer.getDisplayName(), component), true);
        }
        return collection.size();
    }
}

