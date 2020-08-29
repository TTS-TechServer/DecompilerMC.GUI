/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.IntRange;

public class TimeUtil {
    public static IntRange rangeOfSeconds(int n, int n2) {
        return new IntRange(n * 20, n2 * 20);
    }
}

