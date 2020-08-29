/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.Validate
 */
package net.minecraft.util;

import java.util.function.IntConsumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import org.apache.commons.lang3.Validate;

public class BitStorage {
    private static final int[] MAGIC = new int[]{-1, -1, 0, Integer.MIN_VALUE, 0, 0, 0x55555555, 0x55555555, 0, Integer.MIN_VALUE, 0, 1, 0x33333333, 0x33333333, 0, 0x2AAAAAAA, 0x2AAAAAAA, 0, 0x24924924, 0x24924924, 0, Integer.MIN_VALUE, 0, 2, 0x1C71C71C, 0x1C71C71C, 0, 0x19999999, 0x19999999, 0, 390451572, 390451572, 0, 0x15555555, 0x15555555, 0, 0x13B13B13, 0x13B13B13, 0, 306783378, 306783378, 0, 0x11111111, 0x11111111, 0, Integer.MIN_VALUE, 0, 3, 0xF0F0F0F, 0xF0F0F0F, 0, 0xE38E38E, 0xE38E38E, 0, 226050910, 226050910, 0, 0xCCCCCCC, 0xCCCCCCC, 0, 0xC30C30C, 0xC30C30C, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 0xAAAAAAA, 0xAAAAAAA, 0, 171798691, 171798691, 0, 0x9D89D89, 0x9D89D89, 0, 159072862, 159072862, 0, 0x9249249, 0x9249249, 0, 148102320, 148102320, 0, 0x8888888, 0x8888888, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 0x7878787, 0x7878787, 0, 0x7507507, 0x7507507, 0, 0x71C71C7, 0x71C71C7, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 0x6906906, 0x6906906, 0, 0x6666666, 0x6666666, 0, 104755299, 104755299, 0, 0x6186186, 0x6186186, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 0x5B05B05, 0x5B05B05, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 0x5555555, 0x5555555, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 0x5050505, 0x5050505, 0, 0x4EC4EC4, 0x4EC4EC4, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 0x4924924, 0x4924924, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 0x4444444, 0x4444444, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 0x4104104, 0x4104104, 0, Integer.MIN_VALUE, 0, 5};
    private final long[] data;
    private final int bits;
    private final long mask;
    private final int size;
    private final int valuesPerLong;
    private final int divideMul;
    private final int divideAdd;
    private final int divideShift;

    public BitStorage(int n, int n2) {
        this(n, n2, null);
    }

    public BitStorage(int n, int n2, @Nullable long[] arrl) {
        Validate.inclusiveBetween((long)1L, (long)32L, (long)n);
        this.size = n2;
        this.bits = n;
        this.mask = (1L << n) - 1L;
        this.valuesPerLong = (char)(64 / n);
        int n3 = 3 * (this.valuesPerLong - 1);
        this.divideMul = MAGIC[n3 + 0];
        this.divideAdd = MAGIC[n3 + 1];
        this.divideShift = MAGIC[n3 + 2];
        int n4 = (n2 + this.valuesPerLong - 1) / this.valuesPerLong;
        if (arrl != null) {
            if (arrl.length != n4) {
                throw Util.pauseInIde(new RuntimeException("Invalid length given for storage, got: " + arrl.length + " but expected: " + n4));
            }
            this.data = arrl;
        } else {
            this.data = new long[n4];
        }
    }

    private int cellIndex(int n) {
        long l = Integer.toUnsignedLong(this.divideMul);
        long l2 = Integer.toUnsignedLong(this.divideAdd);
        return (int)((long)n * l + l2 >> 32 >> this.divideShift);
    }

    public int getAndSet(int n, int n2) {
        Validate.inclusiveBetween((long)0L, (long)(this.size - 1), (long)n);
        Validate.inclusiveBetween((long)0L, (long)this.mask, (long)n2);
        int n3 = this.cellIndex(n);
        long l = this.data[n3];
        int n4 = (n - n3 * this.valuesPerLong) * this.bits;
        int n5 = (int)(l >> n4 & this.mask);
        this.data[n3] = l & (this.mask << n4 ^ 0xFFFFFFFFFFFFFFFFL) | ((long)n2 & this.mask) << n4;
        return n5;
    }

    public void set(int n, int n2) {
        Validate.inclusiveBetween((long)0L, (long)(this.size - 1), (long)n);
        Validate.inclusiveBetween((long)0L, (long)this.mask, (long)n2);
        int n3 = this.cellIndex(n);
        long l = this.data[n3];
        int n4 = (n - n3 * this.valuesPerLong) * this.bits;
        this.data[n3] = l & (this.mask << n4 ^ 0xFFFFFFFFFFFFFFFFL) | ((long)n2 & this.mask) << n4;
    }

    public int get(int n) {
        Validate.inclusiveBetween((long)0L, (long)(this.size - 1), (long)n);
        int n2 = this.cellIndex(n);
        long l = this.data[n2];
        int n3 = (n - n2 * this.valuesPerLong) * this.bits;
        return (int)(l >> n3 & this.mask);
    }

    public long[] getRaw() {
        return this.data;
    }

    public int getSize() {
        return this.size;
    }

    public void getAll(IntConsumer intConsumer) {
        int n = 0;
        for (long l : this.data) {
            for (int i = 0; i < this.valuesPerLong; ++i) {
                intConsumer.accept((int)(l & this.mask));
                l >>= this.bits;
                if (++n < this.size) continue;
                return;
            }
        }
    }
}

