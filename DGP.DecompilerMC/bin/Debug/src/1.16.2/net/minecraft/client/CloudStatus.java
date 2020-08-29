/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.util.Mth;

public enum CloudStatus {
    OFF(0, "options.off"),
    FAST(1, "options.clouds.fast"),
    FANCY(2, "options.clouds.fancy");

    private static final CloudStatus[] BY_ID;
    private final int id;
    private final String key;

    private CloudStatus(int n2, String string2) {
        this.id = n2;
        this.key = string2;
    }

    public int getId() {
        return this.id;
    }

    public String getKey() {
        return this.key;
    }

    public static CloudStatus byId(int n) {
        return BY_ID[Mth.positiveModulo(n, BY_ID.length)];
    }

    static {
        BY_ID = (CloudStatus[])Arrays.stream(CloudStatus.values()).sorted(Comparator.comparingInt(CloudStatus::getId)).toArray(CloudStatus[]::new);
    }
}

