/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.protocol.login;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.util.Crypt;

public class ServerboundKeyPacket
implements Packet<ServerLoginPacketListener> {
    private byte[] keybytes = new byte[0];
    private byte[] nonce = new byte[0];

    public ServerboundKeyPacket() {
    }

    public ServerboundKeyPacket(SecretKey secretKey, PublicKey publicKey, byte[] arrby) {
        this.keybytes = Crypt.encryptUsingKey(publicKey, secretKey.getEncoded());
        this.nonce = Crypt.encryptUsingKey(publicKey, arrby);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) throws IOException {
        this.keybytes = friendlyByteBuf.readByteArray();
        this.nonce = friendlyByteBuf.readByteArray();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) throws IOException {
        friendlyByteBuf.writeByteArray(this.keybytes);
        friendlyByteBuf.writeByteArray(this.nonce);
    }

    @Override
    public void handle(ServerLoginPacketListener serverLoginPacketListener) {
        serverLoginPacketListener.handleKey(this);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return Crypt.decryptByteToSecretKey(privateKey, this.keybytes);
    }

    public byte[] getNonce(PrivateKey privateKey) {
        if (privateKey == null) {
            return this.nonce;
        }
        return Crypt.decryptUsingKey(privateKey, this.nonce);
    }
}

