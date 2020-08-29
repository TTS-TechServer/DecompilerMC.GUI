/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.entity;

public interface PlayerRideableJumping {
    public void onPlayerJump(int var1);

    public boolean canJump();

    public void handleStartJump(int var1);

    public void handleStopJump();
}

