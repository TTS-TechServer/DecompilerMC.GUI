/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.util.datafix.fixes.References;

public class EntityStringUuidFix
extends DataFix {
    public EntityStringUuidFix(Schema schema, boolean bl) {
        super(schema, bl);
    }

    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityStringUuidFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            Optional optional = dynamic.get("UUID").asString().result();
            if (optional.isPresent()) {
                UUID uUID = UUID.fromString((String)optional.get());
                return dynamic.remove("UUID").set("UUIDMost", dynamic.createLong(uUID.getMostSignificantBits())).set("UUIDLeast", dynamic.createLong(uUID.getLeastSignificantBits()));
            }
            return dynamic;
        }));
    }
}

