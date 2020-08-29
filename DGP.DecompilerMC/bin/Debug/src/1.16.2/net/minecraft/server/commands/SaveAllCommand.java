/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;

public class SaveAllCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.save.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("save-all").requires(commandSourceStack -> commandSourceStack.hasPermission(4))).executes(commandContext -> SaveAllCommand.saveAll((CommandSourceStack)commandContext.getSource(), false))).then(Commands.literal("flush").executes(commandContext -> SaveAllCommand.saveAll((CommandSourceStack)commandContext.getSource(), true))));
    }

    private static int saveAll(CommandSourceStack commandSourceStack, boolean bl) throws CommandSyntaxException {
        commandSourceStack.sendSuccess(new TranslatableComponent("commands.save.saving"), false);
        MinecraftServer minecraftServer = commandSourceStack.getServer();
        minecraftServer.getPlayerList().saveAll();
        boolean bl2 = minecraftServer.saveAllChunks(true, bl, true);
        if (!bl2) {
            throw ERROR_FAILED.create();
        }
        commandSourceStack.sendSuccess(new TranslatableComponent("commands.save.success"), true);
        return 1;
    }
}

