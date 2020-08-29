/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.regex.Matcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.BanIpCommands;
import net.minecraft.server.players.IpBanList;

public class PardonIpCommand {
    private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.invalid"));
    private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon-ip").requires(commandSourceStack -> commandSourceStack.hasPermission(3))).then(Commands.argument("target", StringArgumentType.word()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getIpBans().getUserList(), suggestionsBuilder)).executes(commandContext -> PardonIpCommand.unban((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString((CommandContext)commandContext, (String)"target")))));
    }

    private static int unban(CommandSourceStack commandSourceStack, String string) throws CommandSyntaxException {
        Matcher matcher = BanIpCommands.IP_ADDRESS_PATTERN.matcher(string);
        if (!matcher.matches()) {
            throw ERROR_INVALID.create();
        }
        IpBanList ipBanList = commandSourceStack.getServer().getPlayerList().getIpBans();
        if (!ipBanList.isBanned(string)) {
            throw ERROR_NOT_BANNED.create();
        }
        ipBanList.remove(string);
        commandSourceStack.sendSuccess(new TranslatableComponent("commands.pardonip.success", string), true);
        return 1;
    }
}

