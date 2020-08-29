/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.server.LoggedPrintStream;

public class DebugLoggedPrintStream
extends LoggedPrintStream {
    public DebugLoggedPrintStream(String string, OutputStream outputStream) {
        super(string, outputStream);
    }

    @Override
    protected void logLine(String string) {
        StackTraceElement[] arrstackTraceElement = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = arrstackTraceElement[Math.min(3, arrstackTraceElement.length)];
        LOGGER.info("[{}]@.({}:{}): {}", (Object)this.name, (Object)stackTraceElement.getFileName(), (Object)stackTraceElement.getLineNumber(), (Object)string);
    }
}

