/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.GameRules;

public class GameRuleCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal("gamerule").requires(commandSourceStack -> commandSourceStack.hasPermission(2));
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor(){

            @Override
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                literalArgumentBuilder.then(((LiteralArgumentBuilder)Commands.literal(key.getId()).executes(commandContext -> GameRuleCommand.queryRule((CommandSourceStack)commandContext.getSource(), key))).then(type.createArgument("value").executes(commandContext -> GameRuleCommand.setRule((CommandContext<CommandSourceStack>)commandContext, key))));
            }
        });
        commandDispatcher.register(literalArgumentBuilder);
    }

    private static <T extends GameRules.Value<T>> int setRule(CommandContext<CommandSourceStack> commandContext, GameRules.Key<T> key) {
        CommandSourceStack commandSourceStack = (CommandSourceStack)commandContext.getSource();
        T t = commandSourceStack.getServer().getGameRules().getRule(key);
        ((GameRules.Value)t).setFromArgument(commandContext, "value");
        commandSourceStack.sendSuccess(new TranslatableComponent("commands.gamerule.set", key.getId(), ((GameRules.Value)t).toString()), true);
        return ((GameRules.Value)t).getCommandResult();
    }

    private static <T extends GameRules.Value<T>> int queryRule(CommandSourceStack commandSourceStack, GameRules.Key<T> key) {
        T t = commandSourceStack.getServer().getGameRules().getRule(key);
        commandSourceStack.sendSuccess(new TranslatableComponent("commands.gamerule.query", key.getId(), ((GameRules.Value)t).toString()), false);
        return ((GameRules.Value)t).getCommandResult();
    }
}

