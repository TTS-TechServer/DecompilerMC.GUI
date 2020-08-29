/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.level;

public enum LightLayer {
    SKY(15),
    BLOCK(0);

    public final int surrounding;

    private LightLayer(int n2) {
        this.surrounding = n2;
    }
}

