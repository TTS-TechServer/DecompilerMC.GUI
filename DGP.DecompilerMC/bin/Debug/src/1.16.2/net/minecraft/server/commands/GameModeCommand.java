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
import java.util.Collection;
import java.util.Collections;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;

public class GameModeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal("gamemode").requires(commandSourceStack -> commandSourceStack.hasPermission(2));
        for (GameType gameType : GameType.values()) {
            if (gameType == GameType.NOT_SET) continue;
            literalArgumentBuilder.then(((LiteralArgumentBuilder)Commands.literal(gameType.getName()).executes(commandContext -> GameModeCommand.setMode((CommandContext<CommandSourceStack>)commandContext, Collections.singleton(((CommandSourceStack)commandContext.getSource()).getPlayerOrException()), gameType))).then(Commands.argument("target", EntityArgument.players()).executes(commandContext -> GameModeCommand.setMode((CommandContext<CommandSourceStack>)commandContext, EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "target"), gameType))));
        }
        commandDispatcher.register(literalArgumentBuilder);
    }

    private static void logGamemodeChange(CommandSourceStack commandSourceStack, ServerPlayer serverPlayer, GameType gameType) {
        TranslatableComponent translatableComponent = new TranslatableComponent("gameMode." + gameType.getName());
        if (commandSourceStack.getEntity() == serverPlayer) {
            commandSourceStack.sendSuccess(new TranslatableComponent("commands.gamemode.success.self", translatableComponent), true);
        } else {
            if (commandSourceStack.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
                serverPlayer.sendMessage(new TranslatableComponent("gameMode.changed", translatableComponent), Util.NIL_UUID);
            }
            commandSourceStack.sendSuccess(new TranslatableComponent("commands.gamemode.success.other", serverPlayer.getDisplayName(), translatableComponent), true);
        }
    }

    private static int setMode(CommandContext<CommandSourceStack> commandContext, Collection<ServerPlayer> collection, GameType gameType) {
        int n = 0;
        for (ServerPlayer serverPlayer : collection) {
            if (serverPlayer.gameMode.getGameModeForPlayer() == gameType) continue;
            serverPlayer.setGameMode(gameType);
            GameModeCommand.logGamemodeChange((CommandSourceStack)commandContext.getSource(), serverPlayer, gameType);
            ++n;
        }
        return n;
    }
}
