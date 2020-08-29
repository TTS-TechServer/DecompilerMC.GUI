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
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public class LocateCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.locate.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralArgumentBuilder literalArgumentBuilder = (LiteralArgumentBuilder)Commands.literal("locate").requires(commandSourceStack -> commandSourceStack.hasPermission(2));
        for (Map.Entry entry : StructureFeature.STRUCTURES_REGISTRY.entrySet()) {
            literalArgumentBuilder = (LiteralArgumentBuilder)literalArgumentBuilder.then(Commands.literal((String)entry.getKey()).executes(commandContext -> LocateCommand.locate((CommandSourceStack)commandContext.getSource(), (StructureFeature)entry.getValue())));
        }
        commandDispatcher.register(literalArgumentBuilder);
    }

    private static int locate(CommandSourceStack commandSourceStack, StructureFeature<?> structureFeature) throws CommandSyntaxException {
        BlockPos blockPos = new BlockPos(commandSourceStack.getPosition());
        BlockPos blockPos2 = commandSourceStack.getLevel().findNearestMapFeature(structureFeature, blockPos, 100, false);
        if (blockPos2 == null) {
            throw ERROR_FAILED.create();
        }
        return LocateCommand.showLocateResult(commandSourceStack, structureFeature.getFeatureName(), blockPos, blockPos2, "commands.locate.success");
    }

    public static int showLocateResult(CommandSourceStack commandSourceStack, String string, BlockPos blockPos, BlockPos blockPos2, String string2) {
        int n = Mth.floor(LocateCommand.dist(blockPos.getX(), blockPos.getZ(), blockPos2.getX(), blockPos2.getZ()));
        MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("chat.coordinates", blockPos2.getX(), "~", blockPos2.getZ())).withStyle(style -> style.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + blockPos2.getX() + " ~ " + blockPos2.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip"))));
        commandSourceStack.sendSuccess(new TranslatableComponent(string2, string, mutableComponent, n), false);
        return n;
    }

    private static float dist(int n, int n2, int n3, int n4) {
        int n5 = n3 - n;
        int n6 = n4 - n2;
        return Mth.sqrt(n5 * n5 + n6 * n6);
    }
}

