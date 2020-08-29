/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public class ClientboundKeepAlivePacket
implements Packet<ClientGamePacketListener> {
    private long id;

    public ClientboundKeepAlivePacket() {
    }

    public ClientboundKeepAlivePacket(long l) {
        this.id = l;
    }

    @Override
    public void handle(ClientGamePacketListener clientGamePacketListener) {
        clientGamePacketListener.handleKeepAlive(this);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) throws IOException {
        this.id = friendlyByteBuf.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) throws IOException {
        friendlyByteBuf.writeLong(this.id);
    }

    public long getId() {
        return this.id;
    }
}

