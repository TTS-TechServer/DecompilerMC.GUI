/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.model;

public class ModelUtils {
    public static float rotlerpRad(float f, float f2, float f3) {
        float f4;
        for (f4 = f2 - f; f4 < (float)(-Math.PI); f4 += (float)Math.PI * 2) {
        }
        while (f4 >= (float)Math.PI) {
            f4 -= (float)Math.PI * 2;
        }
        return f + f3 * f4;
    }
}

