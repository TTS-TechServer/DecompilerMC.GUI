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
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SetSpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), Collections.singleton(((CommandSourceStack)commandContext.getSource()).getPlayerOrException()), new BlockPos(((CommandSourceStack)commandContext.getSource()).getPosition()), 0.0f))).then(((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), new BlockPos(((CommandSourceStack)commandContext.getSource()).getPosition()), 0.0f))).then(((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), BlockPosArgument.getOrLoadBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), 0.0f))).then(Commands.argument("angle", AngleArgument.angle()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers((CommandContext<CommandSourceStack>)commandContext, "targets"), BlockPosArgument.getOrLoadBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), AngleArgument.getAngle((CommandContext<CommandSourceStack>)commandContext, "angle")))))));
    }

    private static int setSpawn(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, BlockPos blockPos, float f) {
        ResourceKey<Level> resourceKey = commandSourceStack.getLevel().dimension();
        for (ServerPlayer serverPlayer : collection) {
            serverPlayer.setRespawnPosition(resourceKey, blockPos, f, true, false);
        }
        String string = resourceKey.location().toString();
        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(new TranslatableComponent("commands.spawnpoint.success.single", blockPos.getX(), blockPos.getY(), blockPos.getZ(), Float.valueOf(f), string, collection.iterator().next().getDisplayName()), true);
        } else {
            commandSourceStack.sendSuccess(new TranslatableComponent("commands.spawnpoint.success.multiple", blockPos.getX(), blockPos.getY(), blockPos.getZ(), Float.valueOf(f), string, collection.size()), true);
        }
        return collection.size();
    }
}

