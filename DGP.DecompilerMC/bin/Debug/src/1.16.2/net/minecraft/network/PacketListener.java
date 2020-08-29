/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;

public interface PacketListener {
    public void onDisconnect(Component var1);

    public Connection getConnection();
}

