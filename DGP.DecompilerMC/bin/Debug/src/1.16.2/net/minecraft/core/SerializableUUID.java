/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.minecraft.core;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.Util;

public final class SerializableUUID {
    public static final Codec<UUID> CODEC = Codec.INT_STREAM.comapFlatMap(intStream -> Util.fixedSize(intStream, 4).map(SerializableUUID::uuidFromIntArray), uUID -> Arrays.stream(SerializableUUID.uuidToIntArray(uUID)));

    public static UUID uuidFromIntArray(int[] arrn) {
        return new UUID((long)arrn[0] << 32 | (long)arrn[1] & 0xFFFFFFFFL, (long)arrn[2] << 32 | (long)arrn[3] & 0xFFFFFFFFL);
    }

    public static int[] uuidToIntArray(UUID uUID) {
        long l = uUID.getMostSignificantBits();
        long l2 = uUID.getLeastSignificantBits();
        return SerializableUUID.leastMostToIntArray(l, l2);
    }

    private static int[] leastMostToIntArray(long l, long l2) {
        return new int[]{(int)(l >> 32), (int)l, (int)(l2 >> 32), (int)l2};
    }
}

