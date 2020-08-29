/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.protocol.game;

import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public class ClientboundSetTimePacket
implements Packet<ClientGamePacketListener> {
    private long gameTime;
    private long dayTime;

    public ClientboundSetTimePacket() {
    }

    public ClientboundSetTimePacket(long l, long l2, boolean bl) {
        this.gameTime = l;
        this.dayTime = l2;
        if (!bl) {
            this.dayTime = -this.dayTime;
            if (this.dayTime == 0L) {
                this.dayTime = -1L;
            }
        }
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) throws IOException {
        this.gameTime = friendlyByteBuf.readLong();
        this.dayTime = friendlyByteBuf.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) throws IOException {
        friendlyByteBuf.writeLong(this.gameTime);
        friendlyByteBuf.writeLong(this.dayTime);
    }

    @Override
    public void handle(ClientGamePacketListener clientGamePacketListener) {
        clientGamePacketListener.handleSetTime(this);
    }

    public long getGameTime() {
        return this.gameTime;
    }

    public long getDayTime() {
        return this.dayTime;
    }
}

