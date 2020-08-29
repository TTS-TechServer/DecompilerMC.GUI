/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.ParseResults
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.context.ParsedCommandNode
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  com.mojang.brigadier.tree.CommandNode
 */
package net.minecraft.server.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class HelpCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.help.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("help").executes(commandContext -> {
            Map map = commandDispatcher.getSmartUsage((CommandNode)commandDispatcher.getRoot(), commandContext.getSource());
            for (String string : map.values()) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TextComponent("/" + string), false);
            }
            return map.size();
        })).then(Commands.argument("command", StringArgumentType.greedyString()).executes(commandContext -> {
            ParseResults parseResults = commandDispatcher.parse(StringArgumentType.getString((CommandContext)commandContext, (String)"command"), commandContext.getSource());
            if (parseResults.getContext().getNodes().isEmpty()) {
                throw ERROR_FAILED.create();
            }
            Map map = commandDispatcher.getSmartUsage(((ParsedCommandNode)Iterables.getLast((Iterable)parseResults.getContext().getNodes())).getNode(), commandContext.getSource());
            for (String string : map.values()) {
                ((CommandSourceStack)commandContext.getSource()).sendSuccess(new TextComponent("/" + parseResults.getReader().getString() + " " + string), false);
            }
            return map.size();
        })));
    }
}

