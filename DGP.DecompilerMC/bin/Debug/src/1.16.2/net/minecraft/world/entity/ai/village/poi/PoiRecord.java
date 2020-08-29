/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.minecraft.world.entity.ai.village.poi;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class PoiRecord {
    private final BlockPos pos;
    private final PoiType poiType;
    private int freeTickets;
    private final Runnable setDirty;

    public static Codec<PoiRecord> codec(Runnable runnable) {
        return RecordCodecBuilder.create(instance -> instance.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(poiRecord -> poiRecord.pos), (App)Registry.POINT_OF_INTEREST_TYPE.fieldOf("type").forGetter(poiRecord -> poiRecord.poiType), (App)Codec.INT.fieldOf("free_tickets").orElse((Object)0).forGetter(poiRecord -> poiRecord.freeTickets), (App)RecordCodecBuilder.point((Object)runnable)).apply((Applicative)instance, PoiRecord::new));
    }

    private PoiRecord(BlockPos blockPos, PoiType poiType, int n, Runnable runnable) {
        this.pos = blockPos.immutable();
        this.poiType = poiType;
        this.freeTickets = n;
        this.setDirty = runnable;
    }

    public PoiRecord(BlockPos blockPos, PoiType poiType, Runnable runnable) {
        this(blockPos, poiType, poiType.getMaxTickets(), runnable);
    }

    protected boolean acquireTicket() {
        if (this.freeTickets <= 0) {
            return false;
        }
        --this.freeTickets;
        this.setDirty.run();
        return true;
    }

    protected boolean releaseTicket() {
        if (this.freeTickets >= this.poiType.getMaxTickets()) {
            return false;
        }
        ++this.freeTickets;
        this.setDirty.run();
        return true;
    }

    public boolean hasSpace() {
        return this.freeTickets > 0;
    }

    public boolean isOccupied() {
        return this.freeTickets != this.poiType.getMaxTickets();
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public PoiType getPoiType() {
        return this.poiType;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        return Objects.equals(this.pos, ((PoiRecord)object).pos);
    }

    public int hashCode() {
        return this.pos.hashCode();
    }
}

