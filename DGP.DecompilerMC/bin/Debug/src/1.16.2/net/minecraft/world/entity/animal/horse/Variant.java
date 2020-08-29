/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.entity.animal.horse;

import java.util.Arrays;
import java.util.Comparator;

public enum Variant {
    WHITE(0),
    CREAMY(1),
    CHESTNUT(2),
    BROWN(3),
    BLACK(4),
    GRAY(5),
    DARKBROWN(6);

    private static final Variant[] BY_ID;
    private final int id;

    private Variant(int n2) {
        this.id = n2;
    }

    public int getId() {
        return this.id;
    }

    public static Variant byId(int n) {
        return BY_ID[n % BY_ID.length];
    }

    static {
        BY_ID = (Variant[])Arrays.stream(Variant.values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
    }
}

